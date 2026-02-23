package com.ibosng.gatewayservice.services.impl;

import com.ibosng.gatewayservice.dtos.BerufTreeNode;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.BerufNodeTypes;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BerufService;
import com.ibosng.microsoftgraphservice.config.properties.SharePointProperties;
import com.ibosng.microsoftgraphservice.dtos.FileDetails;
import com.ibosng.microsoftgraphservice.exception.MSGraphServiceException;
import com.ibosng.microsoftgraphservice.services.SharePointService;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.requests.DriveItemCollectionPage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.ibosng.microsoftgraphservice.utils.Helpers.deleteLocalFiles;

@Service
@Slf4j
@RequiredArgsConstructor
public class BerufServiceImpl implements BerufService {

    private final SharePointService sharePointService;
    private final SharePointProperties sharePointProperties;
    public static final String XML_EXTENSION = "xml";


    @Override
    public PayloadResponse fetchBerufe(String searchTerm) {
        PayloadResponse payloadResponse = new PayloadResponse();
        try {
            DriveItemCollectionPage items = sharePointService.getContentsOfFolder(Optional.of(sharePointProperties.getBerufe()));
            List<BerufTreeNode> berufDtos = manageFiles(items, searchTerm);
            PayloadTypeList<BerufTreeNode> payloadTypeList = new PayloadTypeList<>(PayloadTypes.BERUFE.getValue());
            payloadTypeList.setAttributes(berufDtos);
            payloadResponse.setData(List.of(payloadTypeList));
            payloadResponse.setSuccess(true);
        } catch (Exception ex) {
            log.error("Error occurred while checking incoming files", ex);
        }
        return payloadResponse;
    }

    private List<BerufTreeNode> manageFiles(DriveItemCollectionPage items, String searchTerm) {
        List<FileDetails> files = new ArrayList<>();
        for (DriveItem item : items.getCurrentPage()) {
            log.info("Starting to manage the file {}", item.name);
            String filename = item.name + "-" + UUID.randomUUID();
            File file = null;
            try {
                file = sharePointService.downloadFile(item.id, filename);
            } catch (MSGraphServiceException e) {
                log.error("Exception occurred while downloading the file with filename {}", filename, e);
            }
            if (file != null) {
                files.add(new FileDetails(file, filename, file.getAbsolutePath(), item.id, XML_EXTENSION.equals(FilenameUtils.getExtension(filename))));
            }
        }
        List<JSONObject> filesAsJson = readFiles(files);
        List<BerufTreeNode> berufList = extractBerufInfos(filesAsJson, searchTerm);
        deleteLocalFiles(files);
        return berufList;
    }

    public List<BerufTreeNode> extractBerufInfos(List<JSONObject> filesAsJson, String searchTerm) {
        Map<String, JSONObject> jsonMap = extractRelevantJsonObjects(filesAsJson);
        if (!jsonMap.keySet().containsAll(List.of("berufsstruktur", "stammberufe", "spezielle"))) {
            log.error("Missing necessary data: stammberufe, berufsstruktur or spezielle");
            return Collections.emptyList();
        }
        JSONObject berufsstruktur = jsonMap.get("berufsstruktur");
        JSONObject stammberufe = jsonMap.get("stammberufe");
        JSONObject spezielle = jsonMap.get("spezielle");

        List<BerufTreeNode> rootNodes = new ArrayList<>();
        Map<String, BerufTreeNode> berufsfeldMap = mapKategorienToBerufsfelder(berufsstruktur, rootNodes);
        Map<String, List<BerufTreeNode>> spezialisierungenMap = parseSpezialisierungen(spezielle);
        assignBerufeAndSpezialisierungen(stammberufe, berufsfeldMap, spezialisierungenMap);

        if (searchTerm != null && !searchTerm.isBlank()) {
            return filterNodesBySearchTerm(rootNodes, searchTerm);
        }

        return rootNodes;
    }

    private List<BerufTreeNode> filterNodesBySearchTerm(List<BerufTreeNode> nodes, String searchTerm) {
        List<BerufTreeNode> filtered = new ArrayList<>();

        for (BerufTreeNode node : nodes) {
            List<BerufTreeNode> filteredChildren = node.getChildren() != null
                    ? filterNodesBySearchTerm(node.getChildren(), searchTerm)
                    : List.of();
            boolean matches = node.getTitle() != null && node.getTitle().toLowerCase().contains(searchTerm.toLowerCase());

            if (matches || !filteredChildren.isEmpty()) {
                BerufTreeNode copy = new BerufTreeNode();
                copy.setId(node.getId());
                copy.setTitle(node.getTitle());
                copy.setType(node.getType());
                copy.setChildren(filteredChildren.isEmpty() ? null : filteredChildren);
                filtered.add(copy);
            }
        }

        return filtered;
    }

    private Map<String, JSONObject> extractRelevantJsonObjects(List<JSONObject> files) {
        Map<String, JSONObject> result = new HashMap<>();
        for (JSONObject json : files) {
            if (json.has("stammdatenkategorien")) result.put("berufsstruktur", json);
            else if (json.has("stammberufe")) result.put("stammberufe", json);
            else if (json.has("spezielleList")) result.put("spezielle", json);
        }
        return result;
    }

    private void assignBerufeAndSpezialisierungen(JSONObject stammberufe, Map<String, BerufTreeNode> berufsfeldMap, Map<String, List<BerufTreeNode>> spezialisierungenMap) {
        JSONArray stammberufeArray = stammberufe.optJSONObject("stammberufe").getJSONArray("stammberuf");

        for (int i = 0; i < stammberufeArray.length(); i++) {
            JSONObject berufJson = stammberufeArray.getJSONObject(i);
            JSONObject bereich = berufJson.optJSONObject("berufsbereiche").optJSONObject("berufsbereich");
            if (bereich == null) continue;

            JSONObject berufsfeld = bereich.optJSONObject("berufsfeld");
            if (berufsfeld != null) {
                String bfNoteId = berufsfeld.opt("noteid").toString();

                BerufTreeNode berufsfeldNode = berufsfeldMap.get(bfNoteId);
                if (berufsfeldNode != null) {
                    BerufTreeNode berufNode = buildBerufNode(berufJson);
                    List<BerufTreeNode> spezialisierungen = spezialisierungenMap.get(berufNode.getId());
                    if (spezialisierungen != null) {
                        for (BerufTreeNode spez : spezialisierungen) {
                            berufNode.addChild(spez);
                        }
                    }
                    berufsfeldNode.addChild(berufNode);
                }
            }
        }
    }


    private BerufTreeNode buildBerufNode(JSONObject berufJson) {
        BerufTreeNode node = new BerufTreeNode();
        node.setId(berufJson.opt("noteid").toString());
        node.setTitle(berufJson.getString("bezeichnung"));
        node.setType(BerufNodeTypes.BERUF.getValue());
        node.setChildren(new ArrayList<>());
        return node;
    }

    private Map<String, List<BerufTreeNode>> parseSpezialisierungen(JSONObject spezielle) {
        Map<String, List<BerufTreeNode>> spezialisierungenMap = new HashMap<>();
        JSONArray spezielleArray = spezielle.optJSONObject("spezielleList").optJSONArray("spezielle");

        if (spezielleArray == null) {
            return spezialisierungenMap;
        }

        for (int i = 0; i < spezielleArray.length(); i++) {
            JSONObject spezialisierungJson = spezielleArray.getJSONObject(i);
            String stammberufId = spezialisierungJson.optString("stammberuf_noteid");
            BerufTreeNode spezialisierungNode = buildSpezialisierungNode(spezialisierungJson);

            spezialisierungenMap
                    .computeIfAbsent(stammberufId, k -> new ArrayList<>())
                    .add(spezialisierungNode);
        }
        return spezialisierungenMap;
    }

    private BerufTreeNode buildSpezialisierungNode(JSONObject json) {
        BerufTreeNode node = new BerufTreeNode();
        node.setId(json.optString("noteid"));
        node.setTitle(json.optString("bezeichnung"));
        node.setType(BerufNodeTypes.SPEZIALISIERUNG.getValue());
        return node;
    }

    private Map<String, BerufTreeNode> mapKategorienToBerufsfelder(JSONObject berufsstruktur, List<BerufTreeNode> rootNodes) {
        Map<String, BerufTreeNode> berufsfeldMap = new HashMap<>();
        JSONArray kategorien = berufsstruktur.optJSONObject("stammdatenkategorien").optJSONArray("stammdatenkategorie");

        if (kategorien == null) return berufsfeldMap;

        for (int i = 0; i < kategorien.length(); i++) {
            JSONObject kategorieJson = kategorien.getJSONObject(i);
            BerufTreeNode kategorieNode = buildKategorieNode(kategorieJson);

            JSONArray berufsfelder = kategorieJson.optJSONObject("berufsfelder").optJSONArray("berufsfeld");

            if (berufsfelder != null) {
                for (int j = 0; j < berufsfelder.length(); j++) {
                    JSONObject berufsfeldJson = berufsfelder.getJSONObject(j);
                    String berufsfeldNoteId = berufsfeldJson.opt("noteid").toString();
                    BerufTreeNode berufsfeldNode = buildBerufsfeldNode(berufsfeldJson);
                    berufsfeldMap.put(berufsfeldNoteId, berufsfeldNode);
                    kategorieNode.addChild(berufsfeldNode);
                }
            }
            rootNodes.add(kategorieNode);
        }
        return berufsfeldMap;
    }

    private BerufTreeNode buildKategorieNode(JSONObject json) {
        BerufTreeNode node = new BerufTreeNode();
        node.setId(json.opt("noteid").toString());
        node.setTitle(json.getString("bezeichnung"));
        node.setType(BerufNodeTypes.KATEGORIE.getValue());
        node.setChildren(new ArrayList<>());
        return node;
    }

    private BerufTreeNode buildBerufsfeldNode(JSONObject json) {
        BerufTreeNode node = new BerufTreeNode();
        node.setId(json.opt("noteid").toString());
        node.setTitle(json.getString("bezeichnung"));
        node.setType(BerufNodeTypes.BERUFSFELD.getValue());
        node.setChildren(new ArrayList<>());
        return node;
    }


    private List<JSONObject> readFiles(List<FileDetails> files) {
        List<JSONObject> jsonList = new ArrayList<>();
        for (FileDetails fileDetails : files) {
            log.info("Reading the file " + fileDetails.getFilename());
            File file = new File(fileDetails.getFilePath());
            try (FileInputStream fis = new FileInputStream(file)) {
                String xmlContent = new Scanner(fis, StandardCharsets.UTF_8).useDelimiter("\\A").next();
                JSONObject jsonObject = XML.toJSONObject(xmlContent);
                jsonList.add(jsonObject);
            } catch (IOException e) {
                log.error("Failed to read file: " + file.getName(), e);
            } catch (Exception e) {
                log.error("Error converting XML to JSON for file: " + file.getName(), e);
            }
        }
        return jsonList;
    }
}

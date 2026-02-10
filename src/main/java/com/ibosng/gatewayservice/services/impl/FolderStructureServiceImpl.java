package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.BenutzerDetailsService;
import com.ibosng.gatewayservice.services.FolderStructureService;
import com.ibosng.microsoftgraphservice.dtos.TreeNode;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class FolderStructureServiceImpl implements FolderStructureService {

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    private final FileShareService fileShareService;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;
    private final BenutzerDetailsService benutzerDetailsService;


    @Override
    public PayloadResponse fetchFolderStructure(String identifier) {
        PayloadResponse payloadResponse = new PayloadResponse();
        String firma = personalnummerService.findByPersonalnummer(identifier).getFirma().getName();
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(identifier);
        if (stammdaten == null) {
            payloadResponse.setSuccess(false);
            payloadResponse.setMessage("Stammdaten nicht gefunden worden");
            log.error("Stammdaten nicht gefunden worden");
            return payloadResponse;
        }
        if (isNullOrBlank(firma)) {
            payloadResponse.setSuccess(false);
            payloadResponse.setMessage("Firma nicht gefunden worden");
            log.error("Firma nicht gefunden worden");
            return payloadResponse;
        }
        String tempDirectoryPath = firma + "/" + identifier + "_" + stammdaten.getVorname().toUpperCase().replace(" ", "_") + "_" + stammdaten.getNachname().toUpperCase().replace(" ", "_");
        TreeNode targetStructure = fileShareService.getDirectoryStructure(getFileSharePersonalunterlagen(), tempDirectoryPath);
        PayloadTypeList<TreeNode> nodePayloadTypeList = createPayloadForSingleVordienstzeiten(targetStructure);
        if (isNullOrBlank(targetStructure.getErrorMessage())) {
            payloadResponse.setData(List.of(nodePayloadTypeList));
            payloadResponse.setSuccess(true);
        } else {
            payloadResponse.setMessage(targetStructure.getErrorMessage());
            payloadResponse.setSuccess(false);
        }
        return payloadResponse;
    }

    @Override
    public PayloadResponse fetchOwnFolderStructure(String token) {
        Benutzer benutzer = benutzerDetailsService.getUserFromToken(token);
        if (benutzer != null && benutzer.getPersonalnummer() != null) {
            return fetchFolderStructure(benutzer.getPersonalnummer().getPersonalnummer());
        }
        PayloadResponse payloadResponse = new PayloadResponse();
        payloadResponse.setSuccess(false);
        if (benutzer == null) {
            payloadResponse.setMessage("Benutzer nicht gefunden");
        } else {
            payloadResponse.setMessage("Benutzer besitzt keine Personalnummer");
        }

        return payloadResponse;
    }

    private PayloadTypeList<TreeNode> createPayloadForSingleVordienstzeiten(TreeNode treeNode) {
        PayloadTypeList<TreeNode> treeNodePayloadTypeList = new PayloadTypeList<>(PayloadTypes.FOLDER_STRUCTURE.getValue());
        treeNodePayloadTypeList.setAttributes(Collections.singletonList(treeNode));
        return treeNodePayloadTypeList;
    }
}

package com.ibosng.lhrservice.services.impl;

import com.google.common.io.Files;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.utils.Mappers;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.dokumente.DnDokumenteDto;
import com.ibosng.lhrservice.dtos.dokumente.DokumentDto;
import com.ibosng.lhrservice.dtos.dokumente.DokumentRubrikenDto;
import com.ibosng.lhrservice.enums.LhrDocuments;
import com.ibosng.lhrservice.exceptions.LHRWebClientException;
import com.ibosng.lhrservice.services.LHRDokumenteService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.microsoftgraphservice.enums.IbosRole;
import com.ibosng.microsoftgraphservice.services.AzureSSOService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.microsoftgraphservice.services.MailService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;
import static com.ibosng.lhrservice.utils.Constants.DATE_PATTERN_ZEITNACHWEIS;
import static com.ibosng.lhrservice.utils.Constants.REGEX_ZEITNACHWEIS;
import static com.ibosng.lhrservice.utils.Helpers.getFileName;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;
import static com.ibosng.microsoftgraphservice.utils.Helpers.toObjectArray;
import static wiremock.com.google.common.io.Files.getFileExtension;

@Slf4j
@Service
@RequiredArgsConstructor
public class LHRDokumenteServiceImpl implements LHRDokumenteService {
    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private final LHRClient client;
    private final LHREnvironmentService lhrEnvironmentService;
    private final PersonalnummerService personalnummerService;
    private final FileShareService fileShareService;
    private final StammdatenService stammdatenService;
    private final MailService mailService;
    private final AzureSSOService azureSSOService;

    @Override
    public DokumentRubrikenDto findRubrik(Personalnummer personalnummer, String regex) {
        if (personalnummer.getFirma() == null) {
            log.error("No firma for personalnummer: {}", personalnummer.getPersonalnummer());
            return null;
        }
        final Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());
        final String firmaKz = lhrEnvironmentService.getFaKz(personalnummer.getFirma());

        try {
            return client.getDokumenteRubrikiren(firmaKz, firmaNr, regex, null, null).getBody();
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting document rubrik for personalnummer: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public DnDokumenteDto findDocument(Personalnummer personalnummer, String date, String rubrikRegex) {
        final LocalDate parsedDate = (date != null) ? parseDate(date) : null;

        final String startMonth = (parsedDate == null) ?
                LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) :
                parsedDate.withDayOfMonth(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        if (personalnummer.getFirma() == null) {
            log.error("No firma for personalnummer: {}", personalnummer.getPersonalnummer());
            return null;
        }
        final Integer firmaNr = lhrEnvironmentService.getFaNr(personalnummer.getFirma());
        final String firmaKz = lhrEnvironmentService.getFaKz(personalnummer.getFirma());

        DokumentRubrikenDto dokumentRubriken = findRubrik(personalnummer, rubrikRegex);
        if (dokumentRubriken == null || dokumentRubriken.getRubriken().size() != 1) {
            log.error("No zeitnachweis rubrik found for personalnummer: {}", personalnummer.getPersonalnummer());
            return null;
        }
        final Integer rubrikId = dokumentRubriken.getRubriken().get(0).getId();

        try {
            return client.getDokumenteInfo(firmaKz, firmaNr, parseStringToInteger(personalnummer.getPersonalnummer()), rubrikId, startMonth).getBody();
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting document info for personalnummer: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public ResponseEntity<byte[]> downloadZeitnachweisForPersonalnummer(Integer personalnummerId, String date) {
        Personalnummer personalnummerObject = personalnummerService.findById(personalnummerId).orElse(null);
        if (personalnummerObject == null) {
            log.error("Personalnummer object not found for {} in downloadZeitnachweisForPersonalnummer", personalnummerId);
            return ResponseEntity.notFound().build();
        }

        LocalDate filterDate = parseDate(date);

        DnDokumenteDto dnDokumente = findDocument(personalnummerObject, date, REGEX_ZEITNACHWEIS);
        if (dnDokumente == null || dnDokumente.getDocuments().isEmpty()) {
            log.error("No document found for personalnummer: {}", personalnummerObject.getPersonalnummer());
            return ResponseEntity.notFound().build();
        }

        DokumentDto lhrDokument = dnDokumente.getDocuments().stream()
                .filter(document -> !isNullOrBlank(document.getFileName()) && document.getFileName().contains(filterDate.format(DateTimeFormatter.ofPattern(DATE_PATTERN_ZEITNACHWEIS))))
                .findFirst().orElse(null);

        if (lhrDokument == null) {
            log.warn("Dokument for month did not exist: {}", filterDate);
            return ResponseEntity.notFound().build();
        }

        File file;
        try {
            file = client.downloadDokument(
                    dnDokumente.getDienstnehmer().getFaKz(),
                    dnDokumente.getDienstnehmer().getFaNr(),
                    dnDokumente.getDienstnehmer().getDnNr(),
                    dnDokumente.getRubrik().getId(),
                    lhrDokument.getId()
            );
        } catch (LHRWebClientException e) {
            log.error("LHR client returned error for getting document info for personalnummer: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }

        byte[] byteArray;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            byteArray = inputStream.readAllBytes();
        } catch (IOException e) {
            log.error("Error reading file for pnID {}", personalnummerId);
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        Stammdaten stammdaten = stammdatenService.findByPersonalnummerId(personalnummerId);
        final String identifier = stammdaten != null ? stammdaten.getVorname() + "_" + stammdaten.getNachname() : "null";
        final String fileName = getFileName(parseDate(date), identifier, personalnummerObject.getPersonalnummer(), LhrDocuments.ZEITNACHWEISSE.getLhrDocument(), LhrDocuments.ZEITNACHWEISSE, null) + "." + getFileExtension(file.getName());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Disposition", "attachment; filename=" + fileName);
        responseHeaders.set("Content-Type", MediaType.APPLICATION_PDF_VALUE);
        return ResponseEntity.ok().headers(responseHeaders).body(byteArray);
    }

    @Override
    public List<File> getFiles(String personalnummer, String date, String regex) {
        Personalnummer personalnummerObject = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerObject == null) {
            log.error("Personalnummer object not found for {} in getFile", personalnummer);
            return new ArrayList<>();
        }

        DnDokumenteDto dnDokumente = findDocument(personalnummerObject, date, regex);
        if (dnDokumente == null || dnDokumente.getDocuments().isEmpty()) {
            log.error("No document found for personalnummer: {}", personalnummer);
            return new ArrayList<>();
        }

        List<File> downloadedFiles = new ArrayList<>();

        dnDokumente.getDocuments().forEach(document -> {
            try {
                File downloadedFile = Optional.ofNullable(client.downloadDokument(
                        dnDokumente.getDienstnehmer().getFaKz(),
                        dnDokumente.getDienstnehmer().getFaNr(),
                        dnDokumente.getDienstnehmer().getDnNr(),
                        dnDokumente.getRubrik().getId(),
                        document.getId()
                )).map(file -> {
                    File renamedFile = new File(file.getParent(), document.getName() + "_" + file.getName());
                    try {
                        java.nio.file.Files.move(file.toPath(), renamedFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        return renamedFile;
                    } catch (IOException e) {
                        log.error("Failed to move file from {} to {}: {}", file.getAbsolutePath(), renamedFile.getAbsolutePath(), e.getMessage());
                        return null;
                    }
                }).orElse(null);

                if (downloadedFile != null) {
                    downloadedFiles.add(downloadedFile);
                    log.info("Successfully downloaded file: {}", downloadedFile.getAbsolutePath());
                } else {
                    log.warn("Downloaded file was null for document ID: {}", document.getId());
                }

            } catch (LHRWebClientException e) {
                log.error("LHR client error while downloading document ID {} for personalnummer {}: {}",
                        document.getId(), personalnummer, e.getMessage());
                mailService.sendEmail("lhr-service.ma-schnittstelle-nicht-verfuegbar",
                        "german",
                        null,
                        azureSSOService.getGroupMemberEmailsByName(IbosRole.HR.getValue()).toArray(new String[0]),
                        toObjectArray(),
                        toObjectArray());
            }
        });

        if (downloadedFiles.isEmpty()) {
            log.warn("No files were downloaded for personalnummer: {}", personalnummer);
        }

        return downloadedFiles;
    }

    @Override
    public void processAndUploadFiles(String regex, String documentType, LhrDocuments docEnum, String identifier, String personalnummer, LocalDate lastSyncOfDocuments) {
        try {
            List<File> files = getFiles(personalnummer, lastSyncOfDocuments.toString(), regex);
            if (files.isEmpty()) {
                log.info("No {}-document found for {}", documentType, personalnummer);
            } else {
                files.forEach(file -> {
                    if (LhrDocuments.L16.equals(docEnum)) {
                        uploadToFileShare(file, personalnummer, identifier, docEnum);
                    } else {
                        uploadToFileShare(file, personalnummer, identifier, docEnum, documentType);
                    }
                    log.info("Uploaded {}-document: {} for {}", documentType, file.getName(), personalnummer);
                });
            }
        } catch (Exception ex) {
            log.error("Error occured while syncing LHR document {} for personalnummer {} with error {}", docEnum.getLhrDocument(), personalnummer, ex.getMessage());
        }

    }

    @Override
    public boolean uploadToFileShare(File file, String personalnummer, String fullName, LhrDocuments type) {
        return uploadToFileShare(file, personalnummer, fullName, type, type.getLhrDocument());
    }

    @Override
    public boolean uploadToFileShare(File file, String personalnummer, String fullName, LhrDocuments type, String typeName) {
        Personalnummer personalnummerObject = personalnummerService.findByPersonalnummer(personalnummer);
        if (personalnummerObject == null) {
            log.error("Personalnummer object not found for {} in uploadToFileShare", personalnummer);
            return false;
        }

        String firma = personalnummerObject.getFirma().getName();

        String subdirectory = Mappers.getSubdirectoryForDocument(type.getLhrDocument());
        String personalnummerFullName = personalnummer + "_" + fullName;
        String directoryPath = firma + "/" + personalnummerFullName.toUpperCase() + "/" + subdirectory;

        boolean directoryExists = fileShareService.fileExists(getFileSharePersonalunterlagen(), directoryPath);
        if (!directoryExists) {
            log.info("Directory {} does not exist. Creating it now.", directoryPath);
            fileShareService.createStructureForNewMA(personalnummerFullName, getFileSharePersonalunterlagen(), firma);
        }

        try {
            String fileExtension = Files.getFileExtension(file.getName());
            String filename = getFileName(fullName, personalnummer, typeName, type, file.getName()) + "." + fileExtension;

            try (InputStream data = Files.asByteSource(file).openStream()) {
                long length = file.length();

                fileShareService.uploadOrReplaceInFileShare(
                        getFileSharePersonalunterlagen(),
                        directoryPath,
                        filename,
                        data,
                        length
                );
                log.info("File {} uploaded successfully to {}", filename, directoryPath);
            }

            return true;
        } catch (IOException e) {
            log.error("Error while processing file {}: {}", file.getAbsoluteFile(), e.getMessage());
            return false;
        }
    }
}
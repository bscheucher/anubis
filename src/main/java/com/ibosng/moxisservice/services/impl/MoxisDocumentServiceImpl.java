package com.ibosng.moxisservice.services.impl;

import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.services.VereinbarungService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng.moxisservice.clients.MoxisClient;
import com.ibosng.moxisservice.exceptions.MoxisException;
import com.ibosng.moxisservice.services.MoxisDocumentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Mappers.getSubdirectoryForDocument;
import static com.ibosng.microsoftgraphservice.utils.Helpers.updateSubdirectoryName;
import static com.ibosng.moxisservice.utils.Constants.DIENSTVERTRAG;
import static com.ibosng.moxisservice.utils.Helpers.createFileInputStream;
import static com.ibosng.moxisservice.utils.Helpers.getPartFilenameForDV;

@Service
@Slf4j
@RequiredArgsConstructor
public class MoxisDocumentServiceImpl implements MoxisDocumentService {
    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    private final MoxisClient moxisClient;
    private final FileShareService fileShareService;
    private final StammdatenService stammdatenService;
    private static final String SIGNED = "Unterschrieben";
    private static final String VEREINBARUNG = "Vereinbarung";
    private static final String VEREINBARUNG_FILE_EXTENSION = ".pdf";

    private final VereinbarungService vereinbarungService;


    public static String generateVereinbarungFileName(Vereinbarung vereinbarung, String personalnummer, Stammdaten stammdaten) {
        String datum = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String vorname = stammdaten.getVorname();
        String nachname = stammdaten.getNachname();
        String additionalIdentifier = vereinbarung.getVereinbarungName();
        if (additionalIdentifier != null) {
            additionalIdentifier = additionalIdentifier.trim().replaceAll("\\s+", "_");
        } else {
            additionalIdentifier = "";
        }

        return String.format("%s_%s_%s_%s_%s_%s%s",
                        datum,
                        VEREINBARUNG,
                        additionalIdentifier != null && !additionalIdentifier.isBlank() ? additionalIdentifier : "",
                        personalnummer,
                        vorname,
                        nachname,
                        VEREINBARUNG_FILE_EXTENSION
                ).replaceAll("_+", "_")  // Remove consecutive underscores in case identifier is empty
                .replaceAll("_$", "");  // Remove trailing underscore if needed
    }

    @Override
    public ResponseEntity<Resource> getAndUploadDocument(String processInstanceId, String personalnummer) {
        try {
            Mono<File> response = moxisClient.getDocument(processInstanceId);
            File file = response.block();
            if (file != null) {
                Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);

                String firma = stammdaten.getPersonalnummer().getFirma().getName();
                String vorname = stammdaten.getVorname();
                String nachname = stammdaten.getNachname();

                String updatedDirectoryName = updateSubdirectoryName(personalnummer, vorname, nachname);
                String subdirectoryPath = firma + "/" + updatedDirectoryName + "/" + getSubdirectoryForDocument(DIENSTVERTRAG);
                String namePart = getPartFilenameForDV(stammdaten);
                String filename = fileShareService.getFullFileName(getFileSharePersonalunterlagen(), firma, personalnummer, subdirectoryPath, null, namePart);

                fileShareService.uploadOrReplaceInFileShare(
                        fileSharePersonalunterlagen,
                        subdirectoryPath,
                        filename,
                        createFileInputStream(file),
                        file.length()
                );

                log.info("File '{}' uploaded successfully to '{}'.", filename, subdirectoryPath);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            }

            log.error("File retrieved from Moxis is null for processInstanceId: {}", processInstanceId);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (MoxisException e) {
            log.error("Moxis exception occurred: {}", e.getMessage());
            return ResponseEntity.of(ProblemDetail.forStatusAndDetail(e.getHttpStatus(), e.getMessage())).build();
        } catch (IOException e) {
            log.error("IO exception while handling the file upload or preparing response: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Resource> getAndUploadVereinbarung(String processInstanceId, String vereinbarungId) {

        Mono<File> response = moxisClient.getDocument(processInstanceId);
        File file = response.block();
        if (file != null) {
            Optional<Vereinbarung> vereinbarung = vereinbarungService.findById(Integer.valueOf(vereinbarungId));
            if(vereinbarung.isEmpty()){
                log.error("No Vereinbarung found with id: " + vereinbarungId);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            String personalnummer = vereinbarung.get().getPersonalnummer().getPersonalnummer();
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);

            String vereinbarungFileName = generateVereinbarungFileName(vereinbarung.get(), personalnummer, stammdaten);

            String directoryPath = fileShareService.getVereinbarungenDirectory(personalnummer, stammdaten);
            // Use "/" as separator since we are refering to the AZ file share
            directoryPath += "/" + SIGNED;

//
            try{
                InputStream pdfInputStream = new FileInputStream(file);
                fileShareService.uploadOrReplaceInFileShare(
                        getFileSharePersonalunterlagen(),
                        directoryPath,
                        vereinbarungFileName,
                        pdfInputStream,
                        file.length()
                );

                log.info("File '{}' uploaded successfully to '{}'.",  file.getName(), directoryPath);

                InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
                headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);

                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } catch (FileNotFoundException e) {
                log.error("IO exception while handling the file upload or preparing response: {}", e.getMessage());
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return null;
    }
}
package com.ibosng.gatewayservice.services.impl;

import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.services.AdresseService;
import com.ibosng.dbservice.services.mitarbeiter.BankDatenService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VordienstzeitenService;
import com.ibosng.dbservice.utils.Mappers;
import com.ibosng.gatewayservice.dtos.masterdata.FileUpload;
import com.ibosng.gatewayservice.dtos.response.PayloadResponse;
import com.ibosng.gatewayservice.dtos.response.PayloadTypeList;
import com.ibosng.gatewayservice.enums.FileUploadTypes;
import com.ibosng.gatewayservice.enums.PayloadTypes;
import com.ibosng.gatewayservice.services.UploadService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.gatewayservice.utils.Constants.GATEWAY_SERVICE;
import static com.ibosng.gatewayservice.utils.Helpers.getFileExtension;
import static com.ibosng.gatewayservice.utils.Helpers.getFileName;
import static com.ibosng.gatewayservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    @Getter
    @Value("${fileSharePersonalunterlagen:#{null}}")
    private String fileSharePersonalunterlagen;

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    @Getter
    @Value("${storageContainerBankcard:#{null}}")
    private String storageContainerBankcard;

    @Getter
    @Value("${storageContainerECard:#{null}}")
    private String storageContainerECard;

    @Getter
    @Value("${storageContainerArbeitsgenehmigung:#{null}}")
    private String storageContainerArbeitsgenehmigung;

    @Getter
    @Value("${storageContainerFoto:#{null}}")
    private String storageContainerFoto;

    @Getter
    @Value("${storageContainerNachweis:#{null}}")
    private String storageContainerNachweis;

    private final StammdatenService stammdatenService;
    private final BankDatenService bankDatenService;
    private final AdresseService adresseService;
    private final PersonalnummerService personalnummerService;
    private final FileShareService fileShareService;
    private final VordienstzeitenService vordienstzeitenService;

    @Override
    public PayloadResponse uploadFile(MultipartFile file, String type, String identifier, String additionalIdentifier) {
        Personalnummer personalnummerEntity = personalnummerService.findByPersonalnummer(identifier);
        if (personalnummerEntity == null) {
            log.error("Personalnummer {} not found", identifier);
            return createUploadPayloadResponse(createFileUpload(identifier, additionalIdentifier, BlobStatus.NONE, type));
        }

        String firma = personalnummerEntity.getFirma().getName();

        String subdirectory = Mappers.getSubdirectoryForDocument(type);
        String directoryPath = firma + "/" + identifier + "/" + subdirectory;

        boolean directoryExists = fileShareService.fileExists(getFileShareTemp(), directoryPath);
        if (!directoryExists) {
            log.info("Directory {} does not exist. Creating it now.", directoryPath);
            fileShareService.createStructureForNewMA(identifier, getFileShareTemp(), firma);
        }

        String fileExtension;
        try {
            fileExtension = getFileExtension(file.getInputStream(), file.getOriginalFilename());
            String filename = getFileName(identifier, additionalIdentifier, FileUploadTypes.fromValue(type), true) + fileExtension;
            //fileShareService.deleteFromFileShareContainingFilename(getFileShareTemp(), personalnummerEntity.getPersonalnummer(), directoryPath, type);

            if (!file.isEmpty()) {
                try (InputStream data = new BufferedInputStream(file.getInputStream())) {
                    long length = file.getSize();

                    fileShareService.uploadOrReplaceInFileShare(
                            getFileShareTemp(),
                            directoryPath,
                            filename,
                            data,
                            length
                    );
                    log.info("File {} uploaded successfully to {}", filename, directoryPath);
                }
            }
        } catch (IOException e) {
            log.error("Error while processing file {}: {}", file.getOriginalFilename(), e.getMessage());
            return createUploadPayloadResponse(createFileUpload(identifier, additionalIdentifier, BlobStatus.NONE, type));
        }

        FileUpload fileUpload = createFileUpload(identifier, additionalIdentifier, BlobStatus.NOT_VERIFIED, type);
        updateEntity(fileUpload);
        return createUploadPayloadResponse(fileUpload);
    }

    private FileUpload createFileUpload(String identifier, String additionalIdentifier, BlobStatus status, String type) {
        FileUpload fileUpload = new FileUpload();
        fileUpload.setIdentifier(identifier);
        fileUpload.setAdditionalIdentifier(additionalIdentifier);
        fileUpload.setStatus(status);
        fileUpload.setType(FileUploadTypes.fromValue(type));
        return fileUpload;
    }

    private PayloadResponse createUploadPayloadResponse(FileUpload fileUpload) {
        PayloadResponse response = new PayloadResponse();
        response.setSuccess(true);
        PayloadTypeList<FileUpload> fileUploadPayloadType = new PayloadTypeList<>(PayloadTypes.FILE.getValue());
        fileUploadPayloadType.setAttributes(Collections.singletonList(fileUpload));

        response.setData(Collections.singletonList(fileUploadPayloadType));
        return response;
    }

    @Override
    public void updateEntity(FileUpload fileUpload) {
        Stammdaten stammdaten = getStammdaten(fileUpload.getIdentifier());

        switch (fileUpload.getType()) {
            case ECARD -> {
                log.info("Updating entity field eCard");
                stammdaten.setEcardStatus(fileUpload.getStatus());
            }
            case BANKCARD -> {
                log.info("Updating entity field BankCard");
                BankDaten bankDaten = getBankDaten(stammdaten);
                bankDaten.setCard(fileUpload.getStatus());
                bankDaten = bankDatenService.save(bankDaten);
                stammdaten.setBank(bankDaten);
            }
            case ARBEITSGENEHMIGUNG -> {
                log.info("Updating entity field Arbeitsgenehmigung");
                if (stammdaten.getZusatzInfo() != null) {
                    stammdaten.getZusatzInfo().setArbeitsgenehmigungStatus(fileUpload.getStatus());
                }
            }
            case FOTO -> {
                log.info("Updating entity field Foto");
                if (stammdaten.getZusatzInfo() != null) {
                    stammdaten.getZusatzInfo().setFoto(fileUpload.getStatus());
                }
            }
            case VORDIENSTZEITEN_NACHWEIS -> {
                log.info("Updating entity field Nachweis");
                Optional<Vordienstzeiten> vordienstzeiten = vordienstzeitenService.findById(parseStringToInteger(fileUpload.getAdditionalIdentifier()));
                vordienstzeiten.ifPresent(value -> value.setNachweisStatus(fileUpload.getStatus()));
            }
        }
        if (stammdaten.getPersonalnummer() == null) {
            Personalnummer personalnummer = personalnummerService.findByPersonalnummer(fileUpload.getIdentifier());
            stammdaten.setPersonalnummer(personalnummer);
        }
        stammdatenService.save(stammdaten);
    }

    private BankDaten getBankDaten(Stammdaten stammdaten) {
        if (stammdaten.getBank() != null) {
            if (!stammdaten.getBank().getStatus().equals(MitarbeiterStatus.NEW)) {
                stammdaten.getBank().setStatus(MitarbeiterStatus.INACTIVE);
                bankDatenService.save(stammdaten.getBank());

            } else if (stammdaten.getBank().getStatus().equals(MitarbeiterStatus.NEW)) {
                stammdaten.getBank().setChangedBy(GATEWAY_SERVICE);
                stammdaten.setBank(bankDatenService.save(stammdaten.getBank()));
            }
            return stammdaten.getBank();
        } else {
            BankDaten bankDaten = new BankDaten();
            bankDaten.setStatus(MitarbeiterStatus.NEW);
            bankDaten.setCreatedBy(GATEWAY_SERVICE);
            bankDaten = bankDatenService.save(bankDaten);
            return bankDaten;
        }

    }

    private Stammdaten getStammdaten(String personalnummer) {
        if (!isNullOrBlank(personalnummer)) {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(personalnummer);
            return Objects.requireNonNullElseGet(stammdaten, this::createNewStammdaten);
        }
        return createNewStammdaten();
    }

    private Stammdaten createNewStammdaten() {
        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setStatus(MitarbeiterStatus.NEW);
        Adresse adresse = new Adresse();
        adresse.setCreatedBy(GATEWAY_SERVICE);
        adresse.setStatus(Status.NEW);
        adresse = adresseService.save(adresse);
        stammdaten.setAdresse(adresse);
        stammdaten.setCreatedBy(GATEWAY_SERVICE);
        return stammdaten;
    }
}

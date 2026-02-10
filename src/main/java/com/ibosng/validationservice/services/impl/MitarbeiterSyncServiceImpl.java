package com.ibosng.validationservice.services.impl;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import com.ibosng.dbibosservice.entities.IbisFirmaIbos;
import com.ibosng.dbibosservice.services.AdresseIbosService;
import com.ibosng.dbibosservice.services.IbisFirmaIbosService;
import com.ibosng.dbmapperservice.services.MitarbeiterMapperService;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.masterdata.IbisFirmaService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.microsoftgraphservice.services.FileShareService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.MitarbeiterSyncService;
import com.ibosng.validationservice.services.ValidatorService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;
import static com.ibosng.dbservice.utils.Parsers.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MitarbeiterSyncServiceImpl implements MitarbeiterSyncService {

    private final static String TEST_TENANT_UPN_PREFIX = "ibosng.";

    @Getter
    @Value("${fileShareTemp:#{null}}")
    private String fileShareTemp;

    @Value("${isProduction:#{null}}")
    private String isProduction;

    private boolean isProduction() {
        return Boolean.parseBoolean(isProduction);
    }

    @Getter
    @Setter
    private boolean isVertragsdatenExistedBeforeSync = false;

    private static final String ADTYP_TEILNEHMER = "tn";
    private static final String ADTYP_STAMMDATEN = "tr";

    private final MitarbeiterMapperService mitarbeiterMapperService;
    private final PersonalnummerService personalnummerService;
    private final BenutzerService benutzerService;
    private final AdresseIbosService adresseIbosService;
    private final IbisFirmaIbosService ibisFirmaIbosService;
    private final IbisFirmaService ibisFirmaService;
    private final ValidatorService validatorService;
    private final FileShareService fileShareService;
    private final VertragsdatenService vertragsdatenService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public ResponseEntity<String> syncMitarbeiterFromIbisacam(String email, String personalnummerString, Integer ibisFirmaIbos) {
        setVertragsdatenExistedBeforeSync(false);
        Benutzer benutzer = benutzerService.findByEmail(email);
        List<AdresseIbos> adresseIbosList = new ArrayList<>();
        if (!isNullOrBlank(email)) {
            log.info("Searching ADRESSE from work email {}", email);
            adresseIbosList = adresseIbosService.findValidAdresseFromWorkEmail(email);
            if (adresseIbosList.isEmpty()) {
                adresseIbosList = adresseIbosService.findValidAdresseFromWorkEmailAndVertragFix(email);
            }
        }
        AdresseIbos adresseIbos = findFirstObject(adresseIbosList, new HashSet<>(List.of(email)), "AdresseIbos");
        if (adresseIbos == null && !isNullOrBlank(personalnummerString)) {
            log.info("Trying to find ADRESSE from personalnummer {} and ibisFirmaIbos bmdClient {}", personalnummerString, ibisFirmaIbos);
            adresseIbosList = adresseIbosService.findAdresseIbosFromPersonalnummer(personalnummerString, ibisFirmaIbos);
            adresseIbos = findFirstObject(adresseIbosList, new HashSet<>(List.of(email)), "AdresseIbos");
        }
        if (adresseIbos == null && benutzer != null && !isNullOrBlank(benutzer.getFirstName()) && !isNullOrBlank(benutzer.getLastName())) {
            log.info("Could not find ADRESSE from work email {}, searching from first and last name {}, {}", email, benutzer.getFirstName(), benutzer.getLastName());
            adresseIbosList = adresseIbosService.findAllByVornameAndNachname(benutzer.getFirstName(), benutzer.getLastName());
            adresseIbos = findFirstObject(adresseIbosList, new HashSet<>(List.of(email)), "AdresseIbos");
        }
        return syncMAData(adresseIbos, benutzer, personalnummerString, email);
    }

    @Override
    public ResponseEntity<String> syncMitarbeiterFromIbisacamWithUPN(String upn, String personalnummerString, Integer ibisFirmaIbos) {
        setVertragsdatenExistedBeforeSync(false);
        Benutzer benutzer = benutzerService.findByUpn(upn);
        AdresseIbos adresseIbos = null;
        String login = upn;
        if (!isNullOrBlank(login)) {
            log.info("Searching ADRESSE from upn {}", login);
            if (login.contains("@")) {
                login = login.split("@")[0];
            }
            if (!isProduction() && login.contains(TEST_TENANT_UPN_PREFIX)) {
                login = login.replace(TEST_TENANT_UPN_PREFIX, "");
            }
            String adresseFromUPN = adresseIbosService.getAdresseFromUPN(login);
            if (!isNullOrBlank(adresseFromUPN)) {
                Optional<AdresseIbos> adresseIbosOptional = adresseIbosService.findById(parseStringToInteger(adresseFromUPN));
                if (adresseIbosOptional.isPresent()) {
                    adresseIbos = adresseIbosOptional.get();
                }
            }
        }
        return syncMAData(adresseIbos, benutzer, personalnummerString, upn);
    }


    private ResponseEntity<String> syncMAData(AdresseIbos adresseIbos, Benutzer benutzer, String personalnummerString, String upnEmail) {
        boolean isStammdatenSynced = false;
        boolean isVertragsdatenSynced = false;
        Personalnummer personalnummer = null;
        Integer stammdatenId = null;
        if (adresseIbos != null) {
            IbisFirma ibisFirma = findFirmaFromIbisacam(adresseIbos);
            if (ibisFirma != null) {
                StammdatenDto stammdatenDto = mitarbeiterMapperService.mapIbosDataToStammdatenDto(adresseIbos);
                VertragsdatenDto vertragsdatenDto = mitarbeiterMapperService.mapIbosDataToVertragsdatenDto(adresseIbos);
                if (stammdatenDto == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("Stammdaten not found for upn %s and adresseIbos ID %s", upnEmail, adresseIbos.getAdadnr()));
                }
                personalnummer = createPersonalNummer(stammdatenDto, ibisFirma, adresseIbos, upnEmail, personalnummerString);
                stammdatenId = syncStammdaten(stammdatenDto, adresseIbos.getAdadnr(), upnEmail);
                isStammdatenSynced = stammdatenId != null;
                isVertragsdatenSynced = syncVertragsdaten(vertragsdatenDto, personalnummer, adresseIbos.getAdadnr(), upnEmail);
            }
        }
        if (isStammdatenSynced && isVertragsdatenSynced) {
            if (personalnummer != null && !isVertragsdatenExistedBeforeSync) {
                personalnummer.setOnboardedOn(getLocalDateNow());
                personalnummer = personalnummerService.save(personalnummer);

                fileShareService.createStructureForNewMA(personalnummer.getPersonalnummer(), getFileShareTemp(), personalnummer.getFirma().getName());
                fileShareService.renameAndMoveSignedDocumentsAndDirectories(personalnummer.getPersonalnummer());
            }
            if (adresseIbos.getAdtyp().equals(ADTYP_STAMMDATEN)) {
                if (stammdatenId != null) {
                    benutzer = benutzerService.createUserIfNotExists(stammdatenId, getWorkEmail(adresseIbos.getAdemail2(), upnEmail), globalUserHolder.getUsername());
                    benutzer.setPersonalnummer(personalnummer);
                    benutzer.setUpn(upnEmail);
                    benutzerService.save(benutzer);
                }
                return new ResponseEntity<>(personalnummer.getPersonalnummer(), HttpStatus.OK);
            }
            return new ResponseEntity<>(personalnummer.getPersonalnummer(), HttpStatus.OK);
        }
        log.error("Problem occured during syncing MA {} from ibisacam", upnEmail);
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private String getWorkEmail(String workEmail, String upnEmail) {
        if (!isNullOrBlank(workEmail)) {
            return workEmail;
        }
        return upnEmail;
    }

    private Personalnummer createPersonalNummer(StammdatenDto stammdatenDto, IbisFirma ibisFirma, AdresseIbos adresseIbos, String upn, String personalnummerString) {
        log.info("Creating new personalnummer for MA with AdresseIbos ID {} and personalnummer {}", adresseIbos.getAdadnr(), stammdatenDto.getPersonalnummer());
        Personalnummer existingPersonalnummer = personalnummerService.findByPersonalnummer(stammdatenDto.getPersonalnummer());
        if (existingPersonalnummer != null) {
            log.warn("A personalnummer already exist for user {} with AdressIbos ID {}", upn, adresseIbos.getAdadnr());
            if (existingPersonalnummer.getOnboardedOn() == null) {
                existingPersonalnummer.setOnboardedOn(getLocalDateNow());
                personalnummerService.save(existingPersonalnummer);
            }
            return existingPersonalnummer;
        }
        if (!isNullOrBlank(personalnummerString)) {
            existingPersonalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
            log.warn("A personalnummer already exist for user {} with AdressIbos ID {}", upn, adresseIbos.getAdadnr());
            return existingPersonalnummer;
        }
        Personalnummer personalnummer = new Personalnummer();
        if (!isNullOrBlank(personalnummerString)) {
            personalnummer.setPersonalnummer(personalnummerString);
        } else {
            personalnummer.setPersonalnummer(stammdatenDto.getPersonalnummer());
        }
        personalnummer.setFirma(ibisFirma);
        personalnummer.setIsIbosngOnboarded(false);
        if (adresseIbos.getAdtyp().equals(ADTYP_STAMMDATEN)) {
            personalnummer.setMitarbeiterType(MitarbeiterType.MITARBEITER);
        } else {
            personalnummer.setMitarbeiterType(MitarbeiterType.TEILNEHMER);
        }
        personalnummer.setStatus(Status.NEW);
        personalnummer.setCreatedBy(upn);
        return personalnummerService.save(personalnummer);
    }

    private IbisFirma findFirmaFromIbisacam(AdresseIbos adresseIbos) {
        if (adresseIbos.getAdklientid() != null) {
            Optional<IbisFirmaIbos> ibisFirmaIbos = ibisFirmaIbosService.findById(adresseIbos.getAdklientid());
            if (ibisFirmaIbos.isPresent()) {
                return ibisFirmaService.findByBmdClient(ibisFirmaIbos.get().getBmdKlientId());
            }
            log.warn("IbisFirma not found for AdresseIbos ID {} and klientId {}", adresseIbos.getAdadnr(), adresseIbos.getAdklientid());
        }
        log.warn("AdresseIbos {} does not have firma", adresseIbos.getAdadnr());
        return null;
    }

    private Integer syncStammdaten(StammdatenDto stammdatenDto, Integer adresseIbosId, String upn) {
        StammdatenDto stammdatenDtoResponse = validatorService.validateMitarbeiterStammdaten(stammdatenDto, true, upn).getBody();
        if (stammdatenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", stammdatenDto.getPersonalnummer());
            return null;
        } else {
            if (stammdatenDtoResponse.getErrors().isEmpty()) {
                log.info("Validation successfully completed for stammdatenDto with personalnummer {} and for AdressIbos entity with id {}", stammdatenDto.getPersonalnummer(), adresseIbosId);
            } else {
                log.warn("Validation failed for stammdatenDto with personalnummer {} and for AdressIbos entity with id {}", stammdatenDto.getPersonalnummer(), adresseIbosId);
            }
        }
        return stammdatenDto.getId();
    }

    private boolean syncVertragsdaten(VertragsdatenDto vertragsdatenDto, Personalnummer personalnummer, Integer adresseIbosId, String upn) {
        Vertragsdaten vertragsdaten = vertragsdatenService.findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, List.of(MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
        if (vertragsdaten != null) {
            vertragsdatenDto.setId(vertragsdaten.getId()); //used to determine is vertragsdaten existed before sync, pls do not delete it
            setVertragsdatenExistedBeforeSync(true);
        }
        VertragsdatenDto vertragsdatenDtoResponse = validatorService.validateMitarbeiterVertragsdaten(vertragsdatenDto, true, upn).getBody();
        if (vertragsdatenDtoResponse == null) {
            log.error("The response from the validation-service was null for personal nummer:  {}", vertragsdatenDto.getPersonalnummer());
            return false;
        }
        if (vertragsdatenDtoResponse.getErrors().isEmpty()) {
            log.info("Validation successfully completed for vertragsdatenDto with personalnummer {} and for AdressIbos entity with id {}", vertragsdatenDto.getPersonalnummer(), adresseIbosId);
        } else {
            log.warn("Validation failed for vertragsdatenDto with personalnummer {} and for AdressIbos entity with id {}", vertragsdatenDto.getPersonalnummer(), adresseIbosId);
        }

        return true;
    }
}

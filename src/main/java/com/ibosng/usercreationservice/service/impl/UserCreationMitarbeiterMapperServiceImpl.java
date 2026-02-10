package com.ibosng.usercreationservice.service.impl;

import com.ibosng.dbibosservice.services.BenutzerIbosService;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.usercreationservice.dto.DienstortDto;
import com.ibosng.usercreationservice.dto.UserAnlageDto;
import com.ibosng.usercreationservice.exception.TechnicalException;
import com.ibosng.usercreationservice.service.UserCreationMitarbeiterMapperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Service
public class UserCreationMitarbeiterMapperServiceImpl implements UserCreationMitarbeiterMapperService {

    private final BenutzerIbosService benutzerIbosService;

    public UserCreationMitarbeiterMapperServiceImpl(BenutzerIbosService benutzerIbosService) {
        this.benutzerIbosService = benutzerIbosService;
    }

    public UserAnlageDto toDto(Stammdaten stammdaten, Vertragsdaten vertragsdaten) throws TechnicalException {
        UserAnlageDto userAnlageDto = new UserAnlageDto();
        userAnlageDto.setDienstort(new DienstortDto());

        if (stammdaten == null || vertragsdaten == null) {
            log.error("Stammdaten or vertragsdaten are null");
            throw new TechnicalException("Stammdaten or vertragsdaten are null");
        }

        if (stammdaten.getPersonalnummer() != null && !isNullOrBlank(stammdaten.getPersonalnummer().getPersonalnummer())) {
            userAnlageDto.setPersonalnummer(stammdaten.getPersonalnummer().getPersonalnummer());
        } else {
            log.error("Personalnummer is null or blank");
            throw new TechnicalException("Personalnummer is null or blank");
        }

        if (!isNullOrBlank(stammdaten.getVorname())) {
            userAnlageDto.setVorname(stammdaten.getVorname());
        } else {
            log.error("personalnummer {} - Vorname is null or blank", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Vorname is null or blank");
        }

        if (!isNullOrBlank(stammdaten.getNachname())) {
            userAnlageDto.setNachname(stammdaten.getNachname());
        } else {
            log.error("personalnummer {} - Nachname is null or blank", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Nachname is null or blank");
        }

        if (vertragsdaten.getKostenstelle() != null &&
                !isNullOrBlank(vertragsdaten.getKostenstelle().getBezeichnung())) {
            userAnlageDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
        } else {
            log.error("personalnummer {} - Kostenstelle is null or blank", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Kostenstelle is null or blank");
        }

        if (vertragsdaten.getDienstort() != null && vertragsdaten.getDienstort().getAdresse() != null) {
            Adresse adresse = vertragsdaten.getDienstort().getAdresse();
            if (adresse.getPlz() != null && (adresse.getPlz() instanceof Plz plz)) {
                if (plz.getBundesland() != null && !isNullOrBlank(plz.getBundesland().getName())) {
                    userAnlageDto.getDienstort().setBundesland(plz.getBundesland().getName());
                } else {
                    log.error("personalnummer {} - Bundesland is null or blank", userAnlageDto.getPersonalnummer());
                    throw new TechnicalException("Bundesland is null or blank");
                }
                if (plz.getPlz() != null) {
                    userAnlageDto.getDienstort().setPlz(String.valueOf(plz.getPlz()));
                } else {
                    log.error("personalnummer {} - PLZ is null or blank", userAnlageDto.getPersonalnummer());
                    throw new TechnicalException("PLZ is null or blank");
                }
            }
            if (!isNullOrBlank(adresse.getOrt())) {
                if (adresse.getOrt().startsWith("Wien")) {
                    userAnlageDto.getDienstort().setStadt("Wien");
                } else {
                    userAnlageDto.getDienstort().setStadt(adresse.getOrt());
                }
            } else {
                log.error("personalnummer {} - Ort is null or blank", userAnlageDto.getPersonalnummer());
                throw new TechnicalException("Ort is null or blank");
            }
            if (!isNullOrBlank(adresse.getStrasse())) {
                userAnlageDto.getDienstort().setAnschrift(adresse.getStrasse());
            } else {
                log.error("personalnummer {} - Strasse is null or blank", userAnlageDto.getPersonalnummer());
                throw new TechnicalException("Strasse is null or blank");
            }
        } else {
            log.error("personalnummer {} - Dienstort is null", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Dienstort is null");
        }

        if (vertragsdaten.getFuehrungskraft() != null) {
            userAnlageDto.setFuehrungskraft(vertragsdaten.getFuehrungskraft().getEmail());
        } else {
            log.warn("personalnummer {} - Fuerhrungskraft is null or blank", userAnlageDto.getPersonalnummer());
        }
        if (vertragsdaten.getPersonalnummer() != null && vertragsdaten.getPersonalnummer().getFirma() != null) {
            userAnlageDto.setFirma(vertragsdaten.getPersonalnummer().getFirma().getName());
        }

        if (vertragsdaten.getStartcoach() != null) {
            userAnlageDto.setStartcoach(vertragsdaten.getStartcoach().getEmail());
        } else {
            log.warn("personalnummer {} - Startcoach is null or blank", userAnlageDto.getPersonalnummer());
        }

        if (vertragsdaten.getEintritt() != null) {
            userAnlageDto.setEintrittAm(vertragsdaten.getEintritt());
        } else {
            log.error("personalnummer {} - Eintritt is null", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Eintritt is null");
        }

        if (vertragsdaten.getKategorie() != null && !isNullOrBlank(vertragsdaten.getKategorie().getName())) {
            userAnlageDto.setKategorie(vertragsdaten.getKategorie().getName());
        } else {
            log.error("personalnummer {} - Kategorie is null or blank", userAnlageDto.getPersonalnummer());
            throw new TechnicalException("Kategorie is null or blank");
        }

        if (vertragsdaten.getBefristungBis() != null) {
            userAnlageDto.setBefristungBis(vertragsdaten.getBefristungBis());
        } else {
            log.warn("personalnummer {} - BefristungBis is null", userAnlageDto.getPersonalnummer());
        }

        return userAnlageDto;
    }

}

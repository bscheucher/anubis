package com.ibosng.validationservice.services.impl;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.ZusatzInfo;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.AdresseService;
import com.ibosng.dbservice.services.ZusatzInfoService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.services.StammdatenValidatorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Slf4j
@RequiredArgsConstructor
public class StammdatenValidatorServiceImpl implements StammdatenValidatorService {

    private final StammdatenService stammdatenService;
    private final AdresseService adresseService;
    private final ZusatzInfoService zusatzInfoService;
    private final PersonalnummerService personalnummerService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public Stammdaten getStammdaten(StammdatenDto stammdatenDto) {
        if (!isNullOrBlank(stammdatenDto.getPersonalnummer())) {
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerString(stammdatenDto.getPersonalnummer());
            if (stammdaten != null) {
                stammdaten.setErrors(new ArrayList<>());
                stammdaten.setChangedOn(LocalDateTime.now());
                return stammdaten;
            }
            return createNewStammdaten(stammdatenDto.getPersonalnummer());
        }
        return null;
    }

    private Stammdaten createNewStammdaten(String personalnummerString) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(personalnummerString);
        if (personalnummer == null) {
            log.error("Cannot create stammdaten, personalnummer not found: {}", personalnummerString);
            return null;
        }
        Stammdaten stammdaten = new Stammdaten();
        stammdaten.setPersonalnummer(personalnummer);
        stammdaten.setStatus(MitarbeiterStatus.NEW);
        Adresse adresse = new Adresse();
        adresse.setCreatedBy(globalUserHolder.getUsername());
        adresse.setChangedBy(globalUserHolder.getUsername());
        adresse.setStatus(Status.NEW);
        stammdaten.setAdresse(adresseService.save(adresse));
        stammdaten.setCreatedBy(globalUserHolder.getUsername());
        ZusatzInfo zusatzInfo = new ZusatzInfo();
        zusatzInfo.setCreatedBy(globalUserHolder.getUsername());
        zusatzInfo.setStatus(MitarbeiterStatus.NEW);
        stammdaten.setZusatzInfo(zusatzInfoService.save(zusatzInfo));

        log.info("New Stammdaten created for personalnummer {}", personalnummerString);
        return stammdaten;
    }
}

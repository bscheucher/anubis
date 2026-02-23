package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungstyp;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungLeistungserfassungValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {

    private final PersonalnummerService personalnummerService;
    private final LeistungserfassungService leistungserfassungService;

    @Override
    public boolean executeValidation(ZeitbuchungenDto objectT, Zeitbuchung objectV) {
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(objectT.getPersonalnummer());
        if (personalnummer == null) {
            log.warn("Zeitbuchung validation - personalnummer {} not found", objectT.getPersonalnummer());
            return false;
        }
        LocalDate leistungsdatum = parseDate(objectT.getLeistungsdatum());
        if (leistungsdatum == null) {
            log.warn("Zeitbuchung validation - Leistungsdatum {} can not be parsed", objectT.getLeistungsdatum());
            return false;
        }

        objectT.setJahr(leistungsdatum.getYear());
        objectT.setMonat(leistungsdatum.getMonthValue());

        Leistungstyp leistungstyp;
        try {
            leistungstyp = Leistungstyp.fromValue(objectT.getLeistungstyp());
        } catch (Exception e) {
            log.warn("Zeitbuchung validation - Leistungstyp {} can not be parsed", objectT.getLeistungstyp());
            return false;
        }
        Leistungserfassung leistungserfassung = leistungserfassungService
                .findByLeistungstypLeistungsdatumAndPersonalnummer(leistungstyp, leistungsdatum, personalnummer);

        if (leistungserfassung == null) {
            leistungserfassung = leistungserfassungService.save(
                    Leistungserfassung.builder()
                            .leistungstyp(leistungstyp)
                            .leistungsdatum(leistungsdatum)
                            .personalnummer(personalnummer)
                            .createdBy(VALIDATION_SERVICE)
                            .build());
        }

        objectV.setLeistungserfassung(leistungserfassung);
        return true;
    }
}

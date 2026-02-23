package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchungstyp;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungstypService;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungZeitbuchuntypValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {
    private final ZeitbuchungstypService zeitbuchungstypService;

    @Override
    public boolean executeValidation(ZeitbuchungenDto objectT, Zeitbuchung objectV) {
        if (isNullOrBlank(objectT.getTaetigkeit())) {
            log.info("Zeitbuchung validation - taegtiket is blank");
            return false;
        }
        Zeitbuchungstyp zeitbuchuntyp = zeitbuchungstypService.findByType(objectT.getTaetigkeit());
        if (zeitbuchuntyp == null) {
            log.warn("No Zeitbuchungstyp found for taetigkeit {}", objectT.getTaetigkeit());
            return false;
        }
        objectV.setZeitbuchungstyp(zeitbuchuntyp);
        return true;
    }
}

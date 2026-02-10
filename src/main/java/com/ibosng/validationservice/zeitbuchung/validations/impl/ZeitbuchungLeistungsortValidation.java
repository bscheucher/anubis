package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungsort;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungLeistungsortValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {

    @Override
    public boolean executeValidation(ZeitbuchungenDto objectT, Zeitbuchung objectV) {
        if (objectT.getLeistungsort() != null) {
            objectV.setLeistungsort(Leistungsort.fromValue(objectT.getLeistungsort()));
        }
        return true;
    }
}

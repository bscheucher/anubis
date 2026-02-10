package com.ibosng.validationservice.zeitbuchung.validations.impl;

import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseTime;

@Slf4j
@RequiredArgsConstructor
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZeitbuchungTimeValidation implements Validation<ZeitbuchungenDto, Zeitbuchung> {

    @Override
    public boolean executeValidation(ZeitbuchungenDto objectT, Zeitbuchung objectV) {
        if(!isNullOrBlank(objectT.getVon())) {
            objectV.setVon(parseTime(objectT.getVon()));
        }
        if (!isNullOrBlank(objectT.getBis())) {
            objectV.setBis(parseTime(objectT.getBis()));
        }
        if (!isNullOrBlank(objectT.getPauseVon())) {
            objectV.setPauseVon(parseTime(objectT.getPauseVon()));
        }
        if (!isNullOrBlank(objectT.getPauseBis())) {
            objectV.setPauseBis(parseTime(objectT.getPauseBis()));
        }
        return true;
    }
}

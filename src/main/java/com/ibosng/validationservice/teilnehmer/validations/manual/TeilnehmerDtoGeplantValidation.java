package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import static com.ibosng.dbservice.utils.Parsers.*;

@RequiredArgsConstructor
public class TeilnehmerDtoGeplantValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer2Seminar teilnehmer2Seminar) {
        boolean result = true;
        if (!isNullOrBlank(teilnehmerDto.getGeplant())) {
            result = isValidDate(teilnehmerDto.getGeplant());
            if (!result) {
                teilnehmer2Seminar.getTeilnehmer().addError("geplant", "Ungültige Planung angegeben", globalUserHolder.getUsername());
                return false; // Either date is invalid
            } else {
                teilnehmer2Seminar.setGeplant(parseDate(teilnehmerDto.getGeplant()));
            }
        }
        return result;
    }
}

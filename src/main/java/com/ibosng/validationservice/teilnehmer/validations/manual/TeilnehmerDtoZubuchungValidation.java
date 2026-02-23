package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import static com.ibosng.dbservice.utils.Parsers.*;

@RequiredArgsConstructor
public class TeilnehmerDtoZubuchungValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> {

    private final GlobalUserHolder globalUserHolder;

    //ToDO: ENABLE VALIDATION!!!
    @Override
    public boolean shouldValidationRun() {
        return false;
    }

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer2Seminar teilnehmer2Seminar) {
        boolean result = true;
        if (!isNullOrBlank(teilnehmerDto.getZubuchung())) {
            result = isValidDate(teilnehmerDto.getZubuchung());
            if (!result) {
                teilnehmer2Seminar.getTeilnehmer().addError("zubuchung", "Ungültige Zubuchung angegeben", globalUserHolder.getUsername());
                return false;
            } else {
                teilnehmer2Seminar.setZubuchung(parseDate(teilnehmerDto.getZubuchung()));
            }
        }
        return result;
    }
}

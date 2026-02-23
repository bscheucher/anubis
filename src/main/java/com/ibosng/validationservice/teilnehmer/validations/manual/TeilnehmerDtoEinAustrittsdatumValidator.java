package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.*;

@RequiredArgsConstructor
public class TeilnehmerDtoEinAustrittsdatumValidator extends AbstractValidation<TeilnehmerDto, Teilnehmer2Seminar> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean shouldValidationRun() {
        return true;
    }

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer2Seminar teilnehmer2Seminar) {
        if (!isNullOrBlank(teilnehmerDto.getEintritt())) {
            if (!isValidDate(teilnehmerDto.getEintritt())) {
                teilnehmer2Seminar.getTeilnehmer().addError("eintritt", "Ungültiger Eintritt angegeben", globalUserHolder.getUsername());
                return false;
            } else {
                teilnehmer2Seminar.setEintritt(parseDate(teilnehmerDto.getEintritt()));
            }
        }

        if (!isNullOrBlank(teilnehmerDto.getAustritt())) {
            if (!isValidDate(teilnehmerDto.getAustritt())) {
                teilnehmer2Seminar.getTeilnehmer().addError("austritt", "Ungültiger Austritt angegeben", globalUserHolder.getUsername());

                return false;
            } else {
                teilnehmer2Seminar.setAustritt(parseDate(teilnehmerDto.getAustritt()));
            }
        }

        if (!isNullOrBlank(teilnehmerDto.getEintritt()) && !isNullOrBlank(teilnehmerDto.getAustritt())) {
            LocalDate eintrittsdatum = parseDate(teilnehmerDto.getEintritt());
            LocalDate austrittsdatum = parseDate(teilnehmerDto.getAustritt());
            if (austrittsdatum.isBefore(eintrittsdatum)) {
                teilnehmer2Seminar.getTeilnehmer().addError("eintritt", "Ungültiger Eintritt angegeben", globalUserHolder.getUsername());
                teilnehmer2Seminar.getTeilnehmer().addError("austritt", "Ungültiger Austritt angegeben", globalUserHolder.getUsername());
                return false;
            }
        }
        return true;
    }
}

package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoVermittelbarAbValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getVermittelbarAb())) {
            if (isValidDate(teilnehmerDto.getVermittelbarAb())) {
                teilnehmer.setVermittelbarAb(parseDate(teilnehmerDto.getVermittelbarAb()));
                return true;
            }
            teilnehmer.addError("vermittelbarAb", "Ungültiges Datum angegeben", globalUserHolder.getUsername());
            return false;
        }
        return true;
    }
}

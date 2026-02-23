package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.GeschlechtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoGeschlechtValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GeschlechtValidation geschlechtValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getGeschlecht())) {
            Geschlecht geschlecht = geschlechtValidation.getGeschlecht(teilnehmerDto.getGeschlecht());
            if (geschlecht != null) {
                teilnehmer.setGeschlecht(geschlecht);
                return true;
            }
        }
        teilnehmer.addError("geschlecht", "Ungültiges Geschlecht angegeben", globalUserHolder.getUsername());
        return false;
    }
}
package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TeilnehmerDtoTitel2Validation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer teilnehmer) {
        if (!isNullOrBlank(teilnehmerDto.getTitel2())) {
            teilnehmer.setTitel2(teilnehmerDto.getTitel2());
            return true;
        } else {
            teilnehmer.setTitel2(null);
        }
        return false;
    }
}

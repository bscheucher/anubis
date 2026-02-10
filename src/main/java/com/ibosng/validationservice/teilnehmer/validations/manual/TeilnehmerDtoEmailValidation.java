package com.ibosng.validationservice.teilnehmer.validations.manual;

import com.ibosng.dbservice.dtos.teilnehmer.TeilnehmerDto;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import com.ibosng.validationservice.validations.EmailValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class TeilnehmerDtoEmailValidation extends AbstractValidation<TeilnehmerDto, Teilnehmer> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(TeilnehmerDto teilnehmerDto, Teilnehmer objectV) {
        boolean result = true;

        if (!isNullOrBlank(teilnehmerDto.getEmail())) {
            result = EmailValidation.isEmailValid(teilnehmerDto.getEmail());
            if (!result) {
                objectV.addError("email", "Ungültige E-Mail-Adresse angegeben", globalUserHolder.getUsername());
            } else {
                objectV.setEmail(teilnehmerDto.getEmail());
            }
        } else {
            objectV.setEmail(null);
        }
        return result;
    }
}

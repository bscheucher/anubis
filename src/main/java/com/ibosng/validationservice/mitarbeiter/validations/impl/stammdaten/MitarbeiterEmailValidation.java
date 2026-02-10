package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.EmailValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterEmailValidation implements Validation<StammdatenDto, Stammdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (isNullOrBlank(stammdatenDto.getEmail())) {
            stammdaten.addError("email", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        boolean result = EmailValidation.isEmailValid(stammdatenDto.getEmail());
        if (!result) {
            stammdaten.addError("email", "Ungültige E-Mail Adresse", globalUserHolder.getUsername());
        } else {
            stammdaten.setEmail(stammdatenDto.getEmail());
        }
        return result;
    }
}

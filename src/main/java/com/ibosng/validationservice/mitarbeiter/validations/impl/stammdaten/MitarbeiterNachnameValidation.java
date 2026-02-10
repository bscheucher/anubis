package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.NachnameValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterNachnameValidation implements Validation<StammdatenDto, Stammdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getNachname())) {
            boolean result = NachnameValidation.isNachnameValid(stammdatenDto.getNachname());
            if (result) {
                stammdaten.setNachname(stammdatenDto.getNachname());
                return true;
            } else {
                stammdaten.addError("nachname", "Das Feld ist ungültig", globalUserHolder.getUsername());
                return false;
            }
        }
        stammdaten.addError("nachname", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

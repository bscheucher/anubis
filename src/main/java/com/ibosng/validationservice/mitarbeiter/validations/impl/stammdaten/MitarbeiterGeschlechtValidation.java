package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.GeschlechtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterGeschlechtValidation implements Validation<StammdatenDto, Stammdaten> {

    private final GeschlechtValidation geschlechtValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getGeschlecht())) {
            Geschlecht geschlecht = geschlechtValidation.getGeschlecht(stammdatenDto.getGeschlecht());
            if (geschlecht != null) {
                stammdaten.setGeschlecht(geschlecht);
                return true;
            }
        }
        stammdaten.addError("geschlecht", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

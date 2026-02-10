package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.PLZValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterPLZValidation implements Validation<StammdatenDto, Stammdaten> {

    private final PLZValidation plzValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (isNullOrBlank(stammdatenDto.getPlz())) {
            stammdaten.addError("plz", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        Plz plz = plzValidation.validatePlz(stammdatenDto.getPlz());
        if (plz != null) {
            stammdaten.getAdresse().setPlz(plz);
            return true;
        }
        stammdaten.addError("plz", "Ungültige PLZ", globalUserHolder.getUsername());
        return false;
    }
}

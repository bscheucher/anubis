package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.OrtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for VHS, eAMS, MDLC
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterOrtValidation implements Validation<StammdatenDto, Stammdaten> {
    private final OrtValidation ortValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (isNullOrBlank(stammdatenDto.getOrt())) {
            stammdaten.addError("ort", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        String ort = ortValidation.validateOrt(stammdatenDto.getOrt());
        if (ort != null) {
            stammdaten.getAdresse().setOrt(ort);
            return true;
        }
        stammdaten.addError("ort", "Ungültiger Ort", globalUserHolder.getUsername());
        return false;
    }

}

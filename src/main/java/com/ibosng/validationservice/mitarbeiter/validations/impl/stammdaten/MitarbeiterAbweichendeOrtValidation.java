package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.OrtValidation;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Required for VHS, eAMS, MDLC
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterAbweichendeOrtValidation implements Validation<StammdatenDto, Stammdaten> {
    private final OrtValidation ortValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (StringUtils.isBlank(stammdatenDto.getAOrt())) {
            stammdaten.getAbweichendeAdresse().setOrt(null);
            return true;
        }
        String ort = ortValidation.validateOrt(stammdatenDto.getAOrt());
        if (ort != null) {
            stammdaten.getAbweichendeAdresse().setOrt(ort);
            return true;
        }
        stammdaten.addError("aort", "Ungültiger Ort", globalUserHolder.getUsername());
        return false;
    }

}

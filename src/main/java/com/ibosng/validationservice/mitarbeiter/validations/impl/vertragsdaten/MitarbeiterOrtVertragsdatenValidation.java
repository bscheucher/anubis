package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.OrtValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

/**
 * Required for VHS, eAMS, MDLC
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterOrtVertragsdatenValidation implements Validation<VertragsdatenDto, Vertragsdaten> {
    private final OrtValidation ortValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (isNullOrBlank(vertragsdatenDto.getOrt())) {
            vertragsdaten.addError("ort", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        String ort = ortValidation.validateOrt(vertragsdatenDto.getOrt());
        if (ort != null) {
            vertragsdaten.getAdresse().setOrt(ort);
            return true;
        }
        vertragsdaten.addError("ort", "Ungültiger Ort", globalUserHolder.getUsername());
        return false;
    }

}

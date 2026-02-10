package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Plz;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.validations.PLZValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterPLZVertragsdatenValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final PLZValidation plzValidation;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (isNullOrBlank(vertragsdatenDto.getPlz())) {
            vertragsdaten.addError("ort", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        Plz plz = plzValidation.validatePlz(vertragsdatenDto.getPlz());
        if (plz != null) {
            vertragsdaten.getAdresse().setPlz(plz);
            return true;
        }
        vertragsdaten.addError("plz", "Ungültige PLZ", globalUserHolder.getUsername());
        return false;
    }
}

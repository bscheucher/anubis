package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterEintrittValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getEintritt())) {
            if (isValidDate(vertragsdatenDto.getEintritt())) {
                vertragsdaten.setEintritt(parseDate(vertragsdatenDto.getEintritt()));
                return true;
            }
            vertragsdaten.addError("eintritt", "Es ist ein ungültiges Datum", globalUserHolder.getUsername());
            return false;
        }
        vertragsdaten.addError("eintritt", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterUestPauschaleValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {
    private static final String UEST_PAUSCHALE = "ÜST-Pauschale";
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getVereinbarungUEberstunden()) && vertragsdatenDto.getVereinbarungUEberstunden().equals(UEST_PAUSCHALE)) {
            if (vertragsdatenDto.getUestPauschale() == null || (vertragsdatenDto.getUestPauschale() != null && vertragsdatenDto.getUestPauschale() == 0.0)) {
                vertragsdaten.addError("uestPauschale", "Das Feld ist leer", globalUserHolder.getUsername());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}

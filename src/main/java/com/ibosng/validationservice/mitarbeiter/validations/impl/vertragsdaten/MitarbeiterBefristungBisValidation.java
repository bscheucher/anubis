package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterBefristungBisValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (vertragsdatenDto.getIsBefristet() != null && vertragsdatenDto.getIsBefristet()) {
            if (areBefristungBisAndEintritsdatumValid(vertragsdatenDto)) {
                vertragsdaten.setIsBefristet(vertragsdatenDto.getIsBefristet());
                LocalDate befristungsBis = parseDate(vertragsdatenDto.getBefristungBis());
                LocalDate eintrittsdatum = parseDate(vertragsdatenDto.getEintritt());
                if (befristungsBis.isAfter(eintrittsdatum.plusMonths(2)) || befristungsBis.isEqual(eintrittsdatum.plusMonths(2))) {
                    vertragsdaten.setBefristungBis(parseDate(vertragsdatenDto.getBefristungBis()));
                    return true;
                }
            }
            vertragsdaten.addError("befristungBis", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        // Set Befristung to null if isBefristet is set to false
        if (vertragsdatenDto.getIsBefristet() != null && !vertragsdatenDto.getIsBefristet()) {
            vertragsdaten.setIsBefristet(false);
            vertragsdaten.setBefristungBis(null);
        }
        return true;
    }

    private boolean areBefristungBisAndEintritsdatumValid(VertragsdatenDto vertragsdatenDto) {
        boolean isBefristungBisFilled = !isNullOrBlank(vertragsdatenDto.getBefristungBis()) && isValidDate(vertragsdatenDto.getBefristungBis());
        boolean isEintritsdatumBisFilled = !isNullOrBlank(vertragsdatenDto.getEintritt()) && isValidDate(vertragsdatenDto.getEintritt());
        return isBefristungBisFilled && isEintritsdatumBisFilled;
    }
}

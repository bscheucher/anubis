package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isValidDate;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterUBGeburtsdatumValidation extends AbstractValidation<UnterhaltsberechtigteDto, Unterhaltsberechtigte> {

    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(UnterhaltsberechtigteDto unterhaltsberechtigteDto, Unterhaltsberechtigte unterhaltsberechtigte) {
        if (isValidDate(unterhaltsberechtigteDto.getUGeburtsdatum())) {
            unterhaltsberechtigte.setGeburtsdatum(parseDate(unterhaltsberechtigteDto.getUGeburtsdatum()));
            return true;
        }
        unterhaltsberechtigte.addError("ugeburtsdatum", "Ungültiges Datum", globalUserHolder.getUsername());
        return false;
    }
}

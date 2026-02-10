package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.masterdata.Verwandtschaft;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.services.masterdata.VerwandtschaftService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterUBVerwandtschaftValidation implements Validation<UnterhaltsberechtigteDto, Unterhaltsberechtigte> {

    private final VerwandtschaftService verwandtschaftService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(UnterhaltsberechtigteDto unterhaltsberechtigteDto, Unterhaltsberechtigte unterhaltsberechtigte) {
        if (!isNullOrBlank(unterhaltsberechtigteDto.getUVerwandtschaft())) {
            Verwandtschaft verwandtschaft = verwandtschaftService.findByName(unterhaltsberechtigteDto.getUVerwandtschaft());
            if (verwandtschaft != null) {
                unterhaltsberechtigte.setVerwandtschaft(verwandtschaft);
                return true;
            }
            unterhaltsberechtigte.addError("uverwandtschaft", "Das Feld ist ungültig", globalUserHolder.getUsername());
        }
        unterhaltsberechtigte.addError("uverwandtschaft", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

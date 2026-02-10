package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.LandService;
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
public class MitarbeiterStaatsbuergerschaftValidation implements Validation<StammdatenDto, Stammdaten> {

    private final LandService landService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getStaatsbuergerschaft())) {
            Land staatsbuergerschaft = landService.findByLandName(stammdatenDto.getStaatsbuergerschaft());
            if (staatsbuergerschaft != null) {
                stammdaten.setStaatsbuergerschaft(staatsbuergerschaft);
                return true;
            }
        }
        stammdaten.addError("staatsbuergerschaft", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

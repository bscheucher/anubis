package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Familienstand;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.masterdata.FamilienstandService;
import com.ibosng.validationservice.Validation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterFamilienstandValidation implements Validation<StammdatenDto, Stammdaten> {

    private final FamilienstandService familienstandService;

    public MitarbeiterFamilienstandValidation(FamilienstandService familienstandService) {
        this.familienstandService = familienstandService;
    }

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getFamilienstand())) {
            Familienstand familienstand = familienstandService.findByName(stammdatenDto.getFamilienstand());
            if (familienstand != null) {
                stammdaten.setFamilienstand(familienstand);
            }
        } else {
            stammdaten.setFamilienstand(null);
        }
        return true;
    }
}

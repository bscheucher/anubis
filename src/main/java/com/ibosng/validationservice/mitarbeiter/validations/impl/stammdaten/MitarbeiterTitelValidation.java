package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.masterdata.TitelService;
import com.ibosng.validationservice.Validation;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterTitelValidation implements Validation<StammdatenDto, Stammdaten> {

    private final TitelService titelService;

    public MitarbeiterTitelValidation(TitelService titelService) {
        this.titelService = titelService;
    }

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getTitel())) {
            Titel titel1 = titelService.findByName(stammdatenDto.getTitel());
            if (titel1 != null) {
                stammdaten.setTitel(titel1);
            }
        } else {
            stammdaten.setTitel(null);
        }
        if (!isNullOrBlank(stammdatenDto.getTitel2())) {
            Titel titel2 = titelService.findByName(stammdatenDto.getTitel2());
            if (titel2 != null) {
                stammdaten.setTitel2(titel2);
            }
        } else {
            stammdaten.setTitel2(null);
        }
        return true;
    }
}

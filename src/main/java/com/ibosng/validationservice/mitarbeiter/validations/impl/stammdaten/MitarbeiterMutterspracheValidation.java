package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.masterdata.MutterspracheService;
import com.ibosng.validationservice.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterMutterspracheValidation implements Validation<StammdatenDto, Stammdaten> {

    private final MutterspracheService mutterspracheService;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        if (!isNullOrBlank(stammdatenDto.getMuttersprache())) {
            Muttersprache muttersprache = mutterspracheService.findByName(stammdatenDto.getMuttersprache());
            if (muttersprache != null) {
                stammdaten.setMuttersprache(muttersprache);
            }
        }
        return true;
    }
}

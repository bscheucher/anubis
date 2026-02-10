package com.ibosng.validationservice.mitarbeiter.validations.impl.stammdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.InternationalPlz;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.InternationalPlzService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.apache.commons.validator.GenericValidator.isBlankOrNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterForeignAbweichendePlzOrtValidation implements Validation<StammdatenDto, Stammdaten> {

    private final InternationalPlzService internationalPlzService;
    private final LandService landService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        boolean result = true;

        if (!isBlankOrNull(stammdatenDto.getAOrt())) {
            stammdaten.getAbweichendeAdresse().setOrt(stammdatenDto.getAOrt());
        } else {
            stammdaten.getAbweichendeAdresse().setOrt("");
        }

        if (!isBlankOrNull(stammdatenDto.getAPlz())
                && !isBlankOrNull(stammdatenDto.getAOrt())
                && !isBlankOrNull(stammdatenDto.getALand())) {
            final Land land = landService.findByLandName(stammdatenDto.getALand());
            Integer landId = (land == null) ? null : land.getId();
            internationalPlzService
                    .findPlzByPlzOrtLand(stammdatenDto.getAPlz(), stammdatenDto.getAOrt(), landId)
                    .ifPresentOrElse(
                            plz -> stammdaten.getAbweichendeAdresse().setPlz(plz),
                            () -> stammdaten.getAbweichendeAdresse().setPlz(createInternationalPlz(stammdatenDto, land))
                    );
        } else {
            stammdaten.getAbweichendeAdresse().setPlz(null);
        }

        return result;
    }

    private InternationalPlz createInternationalPlz(StammdatenDto stammdatenDto, Land land) {
        InternationalPlz internationalPlz = new InternationalPlz();
        internationalPlz.setPlz(stammdatenDto.getPlz());
        internationalPlz.setOrt(stammdatenDto.getOrt());
        internationalPlz.setLand(land);
        internationalPlz.setCreatedBy(globalUserHolder.getUsername());
        return internationalPlzService.save(internationalPlz);
    }

}

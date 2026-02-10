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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.apache.commons.validator.GenericValidator.isBlankOrNull;

@RequiredArgsConstructor
@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterForeignPlzOrtValidation implements Validation<StammdatenDto, Stammdaten> {

    private final InternationalPlzService internationalPlzService;
    private final LandService landService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(StammdatenDto stammdatenDto, Stammdaten stammdaten) {
        boolean result = true;

        if (!isBlankOrNull(stammdatenDto.getOrt())) {
            stammdaten.getAdresse().setOrt(stammdatenDto.getOrt());
        } else {
            stammdaten.getAdresse().setOrt("");
        }

        if (!isBlankOrNull(stammdatenDto.getPlz())
                && !isBlankOrNull(stammdatenDto.getOrt())
                && !isBlankOrNull(stammdatenDto.getLand())) {
            final Land land = landService.findByLandName(stammdatenDto.getLand());
            Integer landId = (land == null) ? null : land.getId();
            internationalPlzService
                    .findPlzByPlzOrtLand(stammdatenDto.getPlz(), stammdatenDto.getOrt(), landId)
                    .ifPresentOrElse(
                            plz -> stammdaten.getAdresse().setPlz(plz),
                            () -> stammdaten.getAdresse().setPlz(createInternationalPlz(stammdatenDto, land))
                    );
        } else {
            stammdaten.addError("plz", "Ungültige PLZ", globalUserHolder.getUsername());
            result = false;
        }

        return result;
    }

    private InternationalPlz createInternationalPlz(StammdatenDto stammdatenDto, Land land) {
        InternationalPlz internationalPlz = new InternationalPlz();
        internationalPlz.setPlz(stammdatenDto.getPlz());
        internationalPlz.setOrt(stammdatenDto.getOrt());
        internationalPlz.setLand(land);
        internationalPlz.setCreatedBy(globalUserHolder.getUsername());
        internationalPlz = internationalPlzService.save(internationalPlz);
        log.info("{plz after saving} - {}, id-{}", internationalPlz, internationalPlz.getId());
        return internationalPlz;
    }

}
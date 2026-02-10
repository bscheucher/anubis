package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.InternationalPlz;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.InternationalPlzService;
import com.ibosng.dbservice.services.LandService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static org.apache.commons.validator.GenericValidator.isBlankOrNull;

@RequiredArgsConstructor
@Slf4j
@Component
public class MitarbeiterForeignPlzOrtVertragsdatenValidation implements Validation<Vertragsdaten, VertragsdatenDto> {

    private final InternationalPlzService internationalPlzService;
    private final LandService landService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(Vertragsdaten vertragsdaten, VertragsdatenDto vertragsdatenDto) {
        boolean result = true;

        if (!isBlankOrNull(vertragsdatenDto.getOrt())) {
            vertragsdaten.getAdresse().setOrt(vertragsdatenDto.getOrt());
        } else {
            vertragsdaten.getAdresse().setOrt("");
        }

        if (!isBlankOrNull(vertragsdatenDto.getPlz())
                && !isBlankOrNull(vertragsdatenDto.getOrt())
                && !isBlankOrNull(vertragsdatenDto.getLand())) {
            final Land land = landService.findByLandName(vertragsdatenDto.getLand());
            Integer landId = (land == null) ? null : land.getId();
            internationalPlzService
                    .findPlzByPlzOrtLand(vertragsdatenDto.getPlz(), vertragsdatenDto.getOrt(), landId)
                    .ifPresentOrElse(
                            plz -> vertragsdaten.getAdresse().setPlz(plz),
                            () -> vertragsdaten.getAdresse().setPlz(createInternationalPlz(vertragsdatenDto, land))
                    );
        } else {
            vertragsdaten.addError("plz", "Ungültige PLZ", globalUserHolder.getUsername());
            result = false;
        }

        return result;
    }

    private InternationalPlz createInternationalPlz(VertragsdatenDto vertragsdatenDto, Land land) {
        InternationalPlz internationalPlz = new InternationalPlz();
        internationalPlz.setPlz(vertragsdatenDto.getPlz());
        internationalPlz.setOrt(vertragsdatenDto.getOrt());
        internationalPlz.setLand(land);
        internationalPlz.setCreatedBy(globalUserHolder.getUsername());
        internationalPlz.setCreatedOn(getLocalDateNow());
        internationalPlz = internationalPlzService.save(internationalPlz);
        log.info("{plz after saving} - {}, id-{}", internationalPlz, internationalPlz.getId());
        return internationalPlz;
    }

}
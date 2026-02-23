package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Parsers.isLong;
import static com.ibosng.validationservice.utils.Parsers.parseStringToInteger;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterStartcoachValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final BenutzerService benutzerService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getStartcoach())) {
            if (isLong(vertragsdatenDto.getStartcoach())) {
                Optional<Benutzer> startcoach = benutzerService.findById(parseStringToInteger(vertragsdatenDto.getStartcoach()));
                if (startcoach.isPresent()) {
                    vertragsdaten.setStartcoach(startcoach.get());
                    return true;
                }
            } else {
                Benutzer benutzer = benutzerService.findAllBySamIbosName(vertragsdatenDto.getFuehrungskraft()).stream().findFirst().orElse(null);
                if (benutzer != null) {
                    vertragsdaten.setStartcoach(benutzer);
                    return true;
                }
            }
            vertragsdaten.addError("startcoach", "Der Benutzer konnte nicht gefunden werden.", globalUserHolder.getUsername());
            return false;
        }
        return true;
    }
}

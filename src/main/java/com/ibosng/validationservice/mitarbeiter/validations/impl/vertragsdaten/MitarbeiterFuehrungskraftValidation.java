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
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterFuehrungskraftValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final BenutzerService benutzerService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getFuehrungskraft())) {
            if (isLong(vertragsdatenDto.getFuehrungskraft())) {
                Optional<Benutzer> fuehrungskraft = benutzerService.findById(parseStringToInteger(vertragsdatenDto.getFuehrungskraft()));
                if (fuehrungskraft.isPresent()) {
                    vertragsdaten.setFuehrungskraft(fuehrungskraft.get());
                    return true;
                }
            } else {
                Benutzer benutzer = benutzerService.findAllBySamIbosName(vertragsdatenDto.getFuehrungskraft()).stream().findFirst().orElse(null);
                if (benutzer != null) {
                    vertragsdaten.setFuehrungskraft(benutzer);
                    return true;
                }
            }
            vertragsdaten.addError("fuehrungskraft", "Der Benutzer konnte nicht gefunden werden.", globalUserHolder.getUsername());
            return false;
        }
        vertragsdaten.addError("fuehrungskraft", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

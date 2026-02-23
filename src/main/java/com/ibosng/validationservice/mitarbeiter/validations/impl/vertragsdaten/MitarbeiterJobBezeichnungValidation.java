package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Jobbeschreibung;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.JobbeschreibungService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterJobBezeichnungValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final JobbeschreibungService jobbeschreibungService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getJobBezeichnung())) {
            Jobbeschreibung jobbeschreibung = jobbeschreibungService.findByName(vertragsdatenDto.getJobBezeichnung());
            if (jobbeschreibung != null) {
                vertragsdaten.setJobBezeichnung(jobbeschreibung);
                return true;
            }
        }
        vertragsdaten.addError("jobBezeichnung", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}

package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Klasse;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.services.masterdata.KlasseService;
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
public class MitarbeiterKlasseValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final KlasseService klasseService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            if (!isNullOrBlank(vertragsdatenDto.getKlasse())) {
                Klasse klasse = klasseService.findByName(vertragsdatenDto.getKlasse());
                if (klasse != null) {
                    vertragsdaten.setKlasse(klasse);
                    return true;
                }
            }
            vertragsdaten.addError("klasse", "Das Feld ist leer", globalUserHolder.getUsername());
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().contains(TeilnehmerSource.TN_ONBOARDING);
    }
}

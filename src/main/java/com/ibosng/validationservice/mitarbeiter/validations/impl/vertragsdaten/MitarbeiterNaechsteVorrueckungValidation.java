package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.validationservice.utils.ValidationHelpers.isDateInFuture;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterNaechsteVorrueckungValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final GehaltInfoService gehaltInfoService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (vertragsdatenDto.getNaechsteVorrueckung() != null) {
            if (isDateInFuture(vertragsdatenDto.getNaechsteVorrueckung())) {
                GehaltInfo gehaltinfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
                if (gehaltinfo != null) {
                    gehaltinfo.setNaechsteVorrueckung(vertragsdatenDto.getNaechsteVorrueckung());
                    return true;
                }
            }
            vertragsdaten.addError("naechsteVorrueckung", "Das Datum muss in der Zukunft sein.", globalUserHolder.getUsername());
            return false;
        }
        vertragsdaten.addError("naechsteVorrueckung", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().contains(TeilnehmerSource.TN_ONBOARDING);
    }
}

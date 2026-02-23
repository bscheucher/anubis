package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.VerwendungsgruppeService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterGehaltUeberstundenValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {
    private static final List<String> VB5_UEBERSTUNDEN = List.of("All in");
    private final VerwendungsgruppeService verwendungsgruppeService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getVerwendungsgruppe())) {
            Verwendungsgruppe verwendungsgruppe = verwendungsgruppeService.findByName(vertragsdatenDto.getVerwendungsgruppe());
            if ((verwendungsgruppe != null) &&
                    ((verwendungsgruppe.getId() < 6) && !isNullOrBlank(vertragsdatenDto.getVereinbarungUEberstunden()) &&
                            VB5_UEBERSTUNDEN.contains(vertragsdatenDto.getVereinbarungUEberstunden()))) {
                vertragsdaten.addError("vereinbarungUeberstunden", "Ungültiger vereinbarung überstunden", globalUserHolder.getUsername());
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}

package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Abrechnungsgruppe;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.AbrechnungsgruppeService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterAbrechnungsgruppeValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final AbrechnungsgruppeService abrechnungsgruppeService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getAbrechnungsgruppe())) {
            List<Abrechnungsgruppe> foundAbrechnungsgruppen = abrechnungsgruppeService.findByBezeichnung(vertragsdatenDto.getAbrechnungsgruppe());
            if (foundAbrechnungsgruppen.isEmpty()) {
                vertragsdaten.addError("abrechnungsgruppe", "Keine Abrechnungsgruppe gefunden", globalUserHolder.getUsername());
                return false;
            }
            if (foundAbrechnungsgruppen.size() > 1) {
                vertragsdaten.addError("abrechnungsgruppe", "Abrechnungsgruppe kann nicht eindeutig zugewiesen werden", globalUserHolder.getUsername());
                return false;
            }
            if (foundAbrechnungsgruppen.size() == 1) {
                vertragsdaten.setAbrechnungsgruppe(foundAbrechnungsgruppen.get(0));
                return true;
            }
        }
        vertragsdaten.addError("abrechnungsgruppe", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

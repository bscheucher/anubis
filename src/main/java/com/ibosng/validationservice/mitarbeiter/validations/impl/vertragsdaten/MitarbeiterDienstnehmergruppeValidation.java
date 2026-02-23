package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Dienstnehmergruppe;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.DienstnehmergruppeService;
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
public class MitarbeiterDienstnehmergruppeValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final DienstnehmergruppeService dienstnehmergruppeService;
    private final GlobalUserHolder globalUserHolder;


    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getDienstnehmergruppe())) {
            List<Dienstnehmergruppe> foundDienstnehmerGruppen = dienstnehmergruppeService.findByBezeichnung(vertragsdatenDto.getDienstnehmergruppe());
            if (foundDienstnehmerGruppen.isEmpty()) {
                vertragsdaten.addError("dienstnehmergruppe", "Keine Dienstnehmergruppe gefunden", globalUserHolder.getUsername());
            }
            if (foundDienstnehmerGruppen.size() > 1) {
                vertragsdaten.addError("dienstnehmergruppe", "Dienstnehmergruppe kann nicht eindeutig zugewiesen werden", globalUserHolder.getUsername());
            }
            if (foundDienstnehmerGruppen.size() == 1) {
                vertragsdaten.setDienstnehmergruppe(foundDienstnehmerGruppen.get(0));
                return true;
            }
        }
        vertragsdaten.addError("dienstnehmergruppe", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

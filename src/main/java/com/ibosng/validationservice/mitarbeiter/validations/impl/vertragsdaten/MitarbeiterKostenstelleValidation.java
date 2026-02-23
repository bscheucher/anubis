package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.IbisFirma2KostenstelleService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterKostenstelleValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final IbisFirma2KostenstelleService ibisFirma2KostenstelleService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getKostenstelle())) {
            Kostenstelle kostenstelle = ibisFirma2KostenstelleService.findKostenstelleByIbisFirmaNameAndKostenstelleBezeichnung(vertragsdaten.getPersonalnummer().getFirma().getName(), vertragsdatenDto.getKostenstelle());
            if (kostenstelle != null) {
                vertragsdaten.setKostenstelle(kostenstelle);
                return true;
            }
        }
        vertragsdaten.addError("kostenstelle", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Taetigkeit;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.TaetigkeitService;
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
public class MitarbeiterTaetigkeitValidation implements Validation<VertragsdatenDto, Vertragsdaten> {
    private static final String TAETITGKEIT_TN_DEFAULT = "AMS-Lehrteilnehmer";
    private final TaetigkeitService taetigkeitService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getTaetigkeit())) {
            if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
                if (vertragsdatenDto.getTaetigkeit().equals(TAETITGKEIT_TN_DEFAULT)) {
                    return true;
                }
                vertragsdaten.addError("taetigkeit", "Die Tätigkeit für ÜBA TN muss '" + TAETITGKEIT_TN_DEFAULT + "' sein.", globalUserHolder.getUsername());
                return false;
            }
            Taetigkeit taetigkeit = taetigkeitService.findByName(vertragsdatenDto.getTaetigkeit());
            if (taetigkeit != null) {
                vertragsdaten.setTaetigkeit(taetigkeit);
                return true;
            }
        }
        vertragsdaten.addError("taetigkeit", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Kategorie;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.KategorieService;
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
public class MitarbeiterKategorieValidation implements Validation<VertragsdatenDto, Vertragsdaten> {
    private static final String KATEGORIE_TN_DEFAULT = "Lehrling";
    private final KategorieService kategorieService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getKategorie())) {
            if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
                if (vertragsdatenDto.getKategorie().equals(KATEGORIE_TN_DEFAULT)) {
                    return true;
                }
                vertragsdaten.addError("kategorie", "Die Kategorie für ÜBA TN muss '" + KATEGORIE_TN_DEFAULT + "' sein.", globalUserHolder.getUsername());
                return false;
            }
            Kategorie kategorie = kategorieService.findByName(vertragsdatenDto.getKategorie());
            if (kategorie != null) {
                vertragsdaten.setKategorie(kategorie);
                return true;
            }
        }
        vertragsdaten.addError("kategorie", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

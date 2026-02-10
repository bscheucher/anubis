package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Kollektivvertrag;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.KollektivvertragService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class MitarbeiterKollektivvertragValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {
    private static final String KOLLEKTIVVERTRAG_TN_DEFAULT = "AMS-Lehrteilnehmer";
    private final KollektivvertragService kollektivvertragService;
    private final GehaltInfoService gehaltInfoService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getKollektivvertrag())) {
            if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
                if (vertragsdatenDto.getKollektivvertrag().equals(KOLLEKTIVVERTRAG_TN_DEFAULT)) {
                    return true;
                }
                vertragsdaten.addError("kollektivvertrag", "Der Kollektivvertrag für ÜBA TN muss '" + KOLLEKTIVVERTRAG_TN_DEFAULT + "' sein.", globalUserHolder.getUsername());
                return false;
            }
            Kollektivvertrag kollektivvertrag = kollektivvertragService.findByName(vertragsdatenDto.getKollektivvertrag());
            if (kollektivvertrag != null) {
                GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
                if (gehaltInfo != null) {
                    if (gehaltInfo.getVerwendungsgruppe() != null) {
                        gehaltInfo.getVerwendungsgruppe().setKollektivvertrag(kollektivvertrag);
                        return true;
                    } else {
                        gehaltInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                        gehaltInfo.setChangedBy(globalUserHolder.getUsername());
                        gehaltInfo.setChangedOn(getLocalDateNow());
                        gehaltInfoService.save(gehaltInfo);
                        vertragsdaten.addError("verwendungsgruppe", "Verwendungsgruppe ist notwending um den Kollektivvertrag zu bestimmen", globalUserHolder.getUsername());
                        return false;
                    }
                }
            }
        }
        vertragsdaten.addError("kollektivvertrag", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }
}

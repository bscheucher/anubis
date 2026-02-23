package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.entities.mitarbeiter.GehaltInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.masterdata.VerwendungsgruppeService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
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
public class MitarbeiterVerwendungsgruppeValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {
    private static final String VERWENDUNGSGRUPPE_LEHRLINGE = "Lehrlinge";

    private final VerwendungsgruppeService verwendungsgruppeService;
    private final GehaltInfoService gehaltInfoService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.TEILNEHMER)) {
            if (vertragsdatenDto.getVerwendungsgruppe().equals(VERWENDUNGSGRUPPE_LEHRLINGE)) {
                return true;
            }
            vertragsdaten.addError("verwendungsgruppe", "Die Verwendungsgruppe für ÜBA TN muss '" + VERWENDUNGSGRUPPE_LEHRLINGE + "' sein.", globalUserHolder.getUsername());
            return false;
        }
        if (vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.MITARBEITER) && !isNullOrBlank(vertragsdatenDto.getVerwendungsgruppe())) {
            Verwendungsgruppe verwendungsgruppe = verwendungsgruppeService.findByName(vertragsdatenDto.getVerwendungsgruppe());
            return setVerwendungsgruppe(verwendungsgruppe, vertragsdaten);
        }
        vertragsdaten.addError("verwendungsgruppe", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    private boolean setVerwendungsgruppe(Verwendungsgruppe verwendungsgruppe, Vertragsdaten vertragsdaten) {
        if (verwendungsgruppe != null) {
            GehaltInfo gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
            if (gehaltInfo != null) {
                gehaltInfo.setVerwendungsgruppe(verwendungsgruppe);
                return true;
            }
        }
        vertragsdaten.addError("verwendungsgruppe", "Die Verwendungsgruppe wurde nicht gefunden", globalUserHolder.getUsername());
        return false;
    }
}

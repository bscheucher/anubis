package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterType;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.validationservice.utils.Parsers.isDouble;
import static com.ibosng.validationservice.utils.Parsers.parseStringToDouble;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class MitarbeiterWochenstundenValidation implements Validation<VertragsdatenDto, Vertragsdaten> {

    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo == null) {
            arbeitszeitenInfo = createNewArbeitszeitInfo(vertragsdaten);
        }

        boolean isMitarbeiter = vertragsdaten.getPersonalnummer().getMitarbeiterType().equals(MitarbeiterType.MITARBEITER);
        if (!isNullOrBlank(vertragsdatenDto.getWochenstunden())) {
            if (isDouble(vertragsdatenDto.getWochenstunden())) {
                Double wochenstunden = parseStringToDouble(vertragsdatenDto.getWochenstunden());
                if (isMitarbeiter) {
                    if (wochenstunden <= 38 && wochenstunden > 0) {
                        arbeitszeitenInfo.setWochenstunden(vertragsdatenDto.getWochenstunden());

                        arbeitszeitenInfo.setChangedBy(globalUserHolder.getUsername());
                        arbeitszeitenInfo.setChangedOn(getLocalDateNow());
                        arbeitszeitenInfoService.save(arbeitszeitenInfo);

                        return true;
                    } else {
                        vertragsdaten.addError("wochenstunden", "Die Wochenstunden müssen weniger als 38 sein", globalUserHolder.getUsername());

                        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                        arbeitszeitenInfo.setChangedBy(globalUserHolder.getUsername());
                        arbeitszeitenInfo.setChangedOn(getLocalDateNow());
                        arbeitszeitenInfoService.save(arbeitszeitenInfo);

                        return false;
                    }


                } else {
                    if (wochenstunden > 0 && wochenstunden <= 38.5) {
                        return true;
                    } else {

                        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                        arbeitszeitenInfo.setChangedBy(globalUserHolder.getUsername());
                        arbeitszeitenInfo.setChangedOn(getLocalDateNow());
                        arbeitszeitenInfoService.save(arbeitszeitenInfo);

                        vertragsdaten.addError("wochenstunden", "Die Wochenstunden müssen weniger als 38.5 sein", globalUserHolder.getUsername());
                        return false;
                    }
                }
            }
            vertragsdaten.addError("wochenstunden", "Das Feld ist ungültig", globalUserHolder.getUsername());
        }
        vertragsdaten.addError("wochenstunden", "Das Feld ist leer", globalUserHolder.getUsername());
        return false;
    }

    private ArbeitszeitenInfo createNewArbeitszeitInfo(Vertragsdaten vertragsdaten) {
        ArbeitszeitenInfo arbeitszeitenInfo = new ArbeitszeitenInfo();
        arbeitszeitenInfo.setVertragsdaten(vertragsdaten);
        arbeitszeitenInfo.setCreatedBy(globalUserHolder.getUsername());
        arbeitszeitenInfo.setCreatedOn(getLocalDateNow());
        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
        log.info("Creating arbeitszeitenInfo during MitarbeiterWochenstundenValidation");
        return arbeitszeitenInfoService.save(arbeitszeitenInfo);
    }
}

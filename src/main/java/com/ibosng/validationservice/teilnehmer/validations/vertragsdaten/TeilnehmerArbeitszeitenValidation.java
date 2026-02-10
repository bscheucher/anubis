package com.ibosng.validationservice.teilnehmer.validations.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsausmass;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.MitarbeiterDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerSource;
import com.ibosng.dbservice.services.masterdata.ArbeitszeitmodellService;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsausmassService;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsstatusService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.Parsers.isDouble;
import static com.ibosng.validationservice.utils.Parsers.parseStringToDouble;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class TeilnehmerArbeitszeitenValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ArbeitszeitmodellService arbeitszeitmodellService;
    private final BeschaeftigungsstatusService beschaeftigungsstatusService;
    private final BeschaeftigungsausmassService beschaeftigungsausmassService;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getWochenstunden())) {
            if (isDouble(vertragsdatenDto.getWochenstunden())) {
                Double wochenstunden = parseStringToDouble(vertragsdatenDto.getWochenstunden());
                if (wochenstunden <= 38.5 && wochenstunden > 0) {
                    ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
                    if (arbeitszeitenInfo == null) {
                        arbeitszeitenInfo = arbeitszeitenInfoService.createNewArbeitszeitInfo(vertragsdaten, VALIDATION_SERVICE);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void setArbeitszeitmodelBisAndVon(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten, ArbeitszeitenInfo arbeitszeitenInfo) {
        if (vertragsdaten.getErrors().stream().map(MitarbeiterDataStatus::getError).noneMatch("eintritt"::equals)) {
            arbeitszeitenInfo.setArbeitszeitmodellVon(vertragsdaten.getEintritt());
        }

        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodellBis()) && isValidDate(vertragsdatenDto.getArbeitszeitmodellBis())) {
            arbeitszeitenInfo.setArbeitszeitmodellBis(parseDate(vertragsdatenDto.getArbeitszeitmodellBis()));
        } else {
            arbeitszeitenInfo.setArbeitszeitmodellBis(null);
        }
    }


    private void setArbeitszeitenInfo(ArbeitszeitenInfo arbeitszeitenInfo, VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        arbeitszeitenInfo.setCreatedBy(VALIDATION_SERVICE);
        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
        if (!isNullOrBlank(vertragsdatenDto.getAuswahlBegruendungFuerDurchrechner())) {
            arbeitszeitenInfo.setAuswahlBegruendungFuerDurchrechner(vertragsdatenDto.getAuswahlBegruendungFuerDurchrechner());
        }
        if (!isNullOrBlank(vertragsdatenDto.getNotizArbeitszeit())) {
            arbeitszeitenInfo.setNotizArbeitszeit(vertragsdatenDto.getNotizArbeitszeit());
        } else {
            arbeitszeitenInfo.setNotizArbeitszeit("");
        }
        if (!isNullOrBlank(vertragsdatenDto.getSpezielleMittagspausenregelung())) {
            arbeitszeitenInfo.setSpezielleMittagspausenregelung(vertragsdatenDto.getSpezielleMittagspausenregelung());
        } else {
            arbeitszeitenInfo.setSpezielleMittagspausenregelung("");
        }
        if (!isNullOrBlank(vertragsdatenDto.getStundenaenderung())) {
            arbeitszeitenInfo.setStundenaenderung(vertragsdatenDto.getStundenaenderung());
        }
        if (!isNullOrBlank(vertragsdatenDto.getVerwendungsbereichsaenderung())) {
            arbeitszeitenInfo.setVerwendungsbereichsaenderung(vertragsdatenDto.getVerwendungsbereichsaenderung());
        }
        if (!isNullOrBlank(vertragsdatenDto.getStufenwechsel())) {
            arbeitszeitenInfo.setStufenwechsel(vertragsdatenDto.getStufenwechsel());
        }
        if (!isNullOrBlank(vertragsdatenDto.getGeschaeftsbereichsaenderung())) {
            arbeitszeitenInfo.setGeschaeftsbereichsaenderung(vertragsdatenDto.getGeschaeftsbereichsaenderung());
        }
        if (!isNullOrBlank(vertragsdatenDto.getBeschaeftigungsausmass())) {
            Beschaeftigungsausmass beschaeftigungsausmass = beschaeftigungsausmassService.findByName(vertragsdatenDto.getBeschaeftigungsausmass());
            if (beschaeftigungsausmass != null) {
                arbeitszeitenInfo.setBeschaeftigungsausmass(beschaeftigungsausmass);
            }
        }
        if (!isNullOrBlank(vertragsdatenDto.getBeschaeftigungsstatus())) {
            Beschaeftigungsstatus beschaeftigungsstatus = beschaeftigungsstatusService.findByName(vertragsdatenDto.getBeschaeftigungsstatus());
            if (beschaeftigungsstatus != null) {
                arbeitszeitenInfo.setBeschaeftigungsstatus(beschaeftigungsstatus);
            }
        }
        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell())) {
            Arbeitszeitmodell arbeitszeitmodell = arbeitszeitmodellService.findByName(vertragsdatenDto.getArbeitszeitmodell());
            if (arbeitszeitmodell != null) {
                arbeitszeitenInfo.setArbeitszeitmodell(arbeitszeitmodell);
            }
        }
        arbeitszeitenInfo.setUrlaubVorabVereinbart(vertragsdatenDto.getUrlaubVorabVereinbart());
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().contains(TeilnehmerSource.TN_ONBOARDING);
    }
}

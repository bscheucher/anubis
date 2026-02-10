package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsausmass;
import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.MitarbeiterDataStatus;
import com.ibosng.dbservice.services.masterdata.ArbeitszeitmodellService;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsausmassService;
import com.ibosng.dbservice.services.masterdata.BeschaeftigungsstatusService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static com.ibosng.dbservice.utils.Parsers.*;


@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class MitarbeiterArbeitszeitenInfoValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ArbeitszeitmodellService arbeitszeitmodellService;
    private final BeschaeftigungsstatusService beschaeftigungsstatusService;
    private final BeschaeftigungsausmassService beschaeftigungsausmassService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
        if (arbeitszeitenInfo == null) {
            arbeitszeitenInfo = createNewArbeitszeitInfo(vertragsdaten);
        }

        setArbeitszeitenInfo(arbeitszeitenInfo, vertragsdatenDto, vertragsdaten);
        arbeitszeitenInfo.setWochenstunden(vertragsdatenDto.getWochenstunden());

        if (vertragsdatenDto.getArbeitszeitmodell() != null && vertragsdatenDto.getArbeitszeitmodell().toLowerCase().startsWith("durchrechnung")) {
            if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodellBis())) {
                if (isValidDate(vertragsdatenDto.getArbeitszeitmodellBis())) {
                    if (parseDate(vertragsdatenDto.getArbeitszeitmodellBis()).isAfter(LocalDate.now()) &&
                            !isNullOrBlank(vertragsdatenDto.getEintritt()) &&
                            isValidDate(vertragsdatenDto.getEintritt()) &&
                            parseDate(vertragsdatenDto.getArbeitszeitmodellBis()).isAfter(parseDate(vertragsdatenDto.getEintritt()))) {
                        arbeitszeitenInfo.setArbeitszeitmodellBis(parseDate(vertragsdatenDto.getArbeitszeitmodellBis()));
                    } else if (parseDate(vertragsdatenDto.getArbeitszeitmodellBis()).isBefore(LocalDate.now())) {
                        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                        vertragsdaten.addError("arbeitszeitmodellBis", "Das Datum muss in der Zukunft liegen.", globalUserHolder.getUsername());
                    } else if (parseDate(vertragsdatenDto.getArbeitszeitmodellBis()).isAfter(LocalDate.now()) &&
                            !isNullOrBlank(vertragsdatenDto.getEintritt()) &&
                            isValidDate(vertragsdatenDto.getEintritt()) &&
                            parseDate(vertragsdatenDto.getArbeitszeitmodellBis()).isBefore(parseDate(vertragsdatenDto.getEintritt()))) {
                        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                        vertragsdaten.addError("arbeitszeitmodellBis", "Das Datum muss später als Eintrittsdatum sein.", globalUserHolder.getUsername());
                    }
                } else {
                    arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                    vertragsdaten.addError("arbeitszeitmodellBis", "Das Feld ist ungültig", globalUserHolder.getUsername());
                }
            } else {
                arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                vertragsdaten.addError("arbeitszeitmodellBis", "Das Feld ist leer", globalUserHolder.getUsername());
            }
        }
        arbeitszeitenInfoService.save(arbeitszeitenInfo);
        return true;
    }

    private ArbeitszeitenInfo createNewArbeitszeitInfo(Vertragsdaten vertragsdaten) {
        ArbeitszeitenInfo arbeitszeitenInfo = new ArbeitszeitenInfo();
        arbeitszeitenInfo.setVertragsdaten(vertragsdaten);
        arbeitszeitenInfo.setCreatedBy(globalUserHolder.getUsername());
        arbeitszeitenInfo.setCreatedOn(getLocalDateNow());
        arbeitszeitenInfo.setStatus(MitarbeiterStatus.NEW);
        log.info("Creating arbeitszeitenInfo during MitarbeiterArbeitszeitenInfoValidation");
        return arbeitszeitenInfoService.save(arbeitszeitenInfo);
    }

    private void setArbeitszeitenInfo(ArbeitszeitenInfo arbeitszeitenInfo, VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        arbeitszeitenInfo.setChangedBy(globalUserHolder.getUsername());
        arbeitszeitenInfo.setChangedOn(getLocalDateNow());
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
            } else {
                arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                vertragsdaten.addError("beschaeftigungsausmass", "Das Feld ist ungültig", globalUserHolder.getUsername());
            }
        } else {
            arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
            vertragsdaten.addError("beschaeftigungsausmass", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (!isNullOrBlank(vertragsdatenDto.getBeschaeftigungsstatus())) {
            Beschaeftigungsstatus beschaeftigungsstatus = beschaeftigungsstatusService.findByName(vertragsdatenDto.getBeschaeftigungsstatus());
            if (beschaeftigungsstatus != null) {
                arbeitszeitenInfo.setBeschaeftigungsstatus(beschaeftigungsstatus);
            } else {
                arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                vertragsdaten.addError("beschaeftigungsstatus", "Das Feld ist ungültig", globalUserHolder.getUsername());
            }
        } else {
            arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
            vertragsdaten.addError("beschaeftigungsstatus", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell())) {
            Arbeitszeitmodell arbeitszeitmodell = arbeitszeitmodellService.findByName(vertragsdatenDto.getArbeitszeitmodell());
            if (arbeitszeitmodell != null) {
                arbeitszeitenInfo.setArbeitszeitmodell(arbeitszeitmodell);
            } else {
                arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
                vertragsdaten.addError("arbeitszeitmodell", "Das Modell konnte nicht gefunden werden", globalUserHolder.getUsername());
            }
        } else {
            arbeitszeitenInfo.setStatus(MitarbeiterStatus.NOT_VALIDATED);
            vertragsdaten.addError("arbeitszeitmodell", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (vertragsdaten.getErrors().stream().map(MitarbeiterDataStatus::getError).noneMatch("eintritt"::equals)) {
            arbeitszeitenInfo.setArbeitszeitmodellVon(vertragsdaten.getEintritt());
        }
        arbeitszeitenInfo.setUrlaubVorabVereinbart(vertragsdatenDto.getUrlaubVorabVereinbart());
    }

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }
}
package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VordienstzeitenDto;
import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.entities.mitarbeiter.BlobStatus;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vordienstzeiten;
import com.ibosng.dbservice.services.masterdata.VertragsartService;
import com.ibosng.validationservice.Validation;
import com.ibosng._config.GlobalUserHolder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;

import static com.ibosng.validationservice.utils.Constants.DAYS_IN_YEAR;
import static com.ibosng.validationservice.utils.Constants.FIX_VERTRAGSART;
import static com.ibosng.validationservice.utils.Constants.MONTHS_IN_YEAR;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Component
@RequiredArgsConstructor
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MitarbeiterVordienstzeitenValidation implements Validation<VordienstzeitenDto, Vordienstzeiten> {

    @Getter
    @Value("${kvCalculationFullTimeHours}")
    private Double fullTimeHours;

    private final VertragsartService vertragsartService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VordienstzeitenDto vordienstzeitenDto, Vordienstzeiten vordienstzeiten) {
        vordienstzeiten.setAnrechenbar(vordienstzeitenDto.isAnrechenbar());
        Vertragsart vertragsart = vertragsartService.findByName(vordienstzeitenDto.getVertragsart());
        if (vertragsart != null) {
            vordienstzeiten.setVertragsart(vertragsart);
        } else {
            vordienstzeiten.addError("vertragsart", "Das Feld ist leer", globalUserHolder.getUsername());
        }
//        vordienstzeiten.setVertragsdaten(vertragsdaten);
        if (!isNullOrBlank(vordienstzeitenDto.getFirma())) {
            vordienstzeiten.setFirma(vordienstzeitenDto.getFirma());
        } else {
            vordienstzeiten.addError("firma", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vordienstzeitenDto.getVordienstzeitenVon())) {
            vordienstzeiten.addError("vordienstzeitenVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vordienstzeitenDto.getVordienstzeitenBis())) {
            vordienstzeiten.addError("vordienstzeitenBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (!isNullOrBlank(vordienstzeitenDto.getNachweisFilename())) {
            vordienstzeiten.setNachweisFileName(vordienstzeitenDto.getNachweisFilename());
        }
        if (!isNullOrBlank(vordienstzeitenDto.getVordienstzeitenVon()) && !isNullOrBlank(vordienstzeitenDto.getVordienstzeitenBis()) &&
                parseDate(vordienstzeitenDto.getVordienstzeitenVon()).isAfter(parseDate(vordienstzeitenDto.getVordienstzeitenBis()))) {
            vordienstzeiten.addError("vordienstzeitenVon", "Das Datum 'von' muss vor dem Datum 'bis' liegen", globalUserHolder.getUsername());
            vordienstzeiten.addError("vordienstzeitenBis", "Das Datum 'bis' muss nach dem Datum 'von' liegen", globalUserHolder.getUsername());
        }

        if (vordienstzeiten.getErrors().stream().filter(datastatus -> datastatus.getError().equals("vordienstzeitenVon") || datastatus.getError().equals("vordienstzeitenBis")).toList().isEmpty()) {
            if (parseDate(vordienstzeitenDto.getVordienstzeitenVon()).isBefore(parseDate(vordienstzeitenDto.getVordienstzeitenBis()))) {
                vordienstzeiten.setVon(parseDate(vordienstzeitenDto.getVordienstzeitenVon()));
                vordienstzeiten.setBis(parseDate(vordienstzeitenDto.getVordienstzeitenBis()));
            } else {
                vordienstzeiten.addError("vordienstzeitenVon", "Das Datum muss bevor dem Datum Bis sein!", globalUserHolder.getUsername());
                vordienstzeiten.addError("vordienstzeitenBis", "Das Datum muss nach den Datum Vor sein!", globalUserHolder.getUsername());
            }
        }

        if (!isNullOrBlank(vordienstzeitenDto.getVWochenstunden()) &&
                (Double.parseDouble(vordienstzeitenDto.getVWochenstunden()) <= 40) &&
                (Double.parseDouble(vordienstzeitenDto.getVWochenstunden()) >= 0)) {
            vordienstzeiten.setWochenstunden(Double.valueOf(vordienstzeitenDto.getVWochenstunden()));
        } else {
            vordienstzeiten.addError("vwochenstunden", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        vordienstzeiten.setAnrechenbar(vordienstzeitenDto.isAnrechenbar());
        if (vordienstzeiten.isAnrechenbar() && (isNullOrBlank(vordienstzeitenDto.getNachweis()) || BlobStatus.fromValue(vordienstzeitenDto.getNachweis()).equals(BlobStatus.NONE))) {
            vordienstzeiten.addError("nachweis", "Anrechenbar ist gesetzt aber keine Datei ist hochgeladen", globalUserHolder.getUsername());
        } else if (!vordienstzeiten.isAnrechenbar() && isNullOrBlank(vordienstzeitenDto.getNachweis())) {
            vordienstzeiten.setNachweisStatus(BlobStatus.NONE);
        }
        if (!isNullOrBlank(vordienstzeitenDto.getNachweis())) {
            vordienstzeiten.setNachweisStatus(BlobStatus.fromValue(vordienstzeitenDto.getNachweis()));
        }
        vordienstzeiten.setCreatedBy(globalUserHolder.getUsername());
        vordienstzeiten.setStatus(MitarbeiterStatus.NEW);
        if (vordienstzeiten.getErrors().isEmpty()) {
            setFacheinschlaegig(vordienstzeitenDto, vordienstzeiten);
            return true;
        }
        return false;
    }

    private void setFacheinschlaegig(VordienstzeitenDto vordienstzeitenDto, Vordienstzeiten vordienstzeiten) {
        if (vordienstzeiten.getVon() == null || vordienstzeiten.getBis() == null) {
            return;
        }

        long daysBetween = ChronoUnit.DAYS.between(vordienstzeiten.getVon(), vordienstzeiten.getBis()) + 1;
        double facheinschlaegig;

        if (vordienstzeiten.getVertragsart().getName().equals(FIX_VERTRAGSART)) {
            facheinschlaegig = calculateFacheinschlaegig(daysBetween, MONTHS_IN_YEAR);
        } else if (vordienstzeiten.getWochenstunden() != null) {
            double fullTimeRatio = vordienstzeiten.getWochenstunden() / getFullTimeHours();
            facheinschlaegig = calculateFacheinschlaegig(daysBetween, fullTimeRatio * MONTHS_IN_YEAR);
        } else {
            return;
        }

        vordienstzeiten.setFacheinschlaegig(roundToOneDecimal(facheinschlaegig));
    }

    private double calculateFacheinschlaegig(long daysBetween, double factor) {
        return (daysBetween / DAYS_IN_YEAR) * factor;
    }

    private double roundToOneDecimal(double value) {
        return BigDecimal.valueOf(value)
                .setScale(1, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

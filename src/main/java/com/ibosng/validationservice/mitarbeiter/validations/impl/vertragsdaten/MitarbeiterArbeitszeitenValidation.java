package com.ibosng.validationservice.mitarbeiter.validations.impl.vertragsdaten;

import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.VertragsdatenDataStatus;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenService;
import com.ibosng._config.GlobalUserHolder;
import com.ibosng.validationservice.teilnehmer.validations.AbstractValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.*;
import static com.ibosng.validationservice.utils.Constants.VALIDATION_SERVICE;
import static com.ibosng.validationservice.utils.Parsers.isDouble;
import static com.ibosng.validationservice.utils.Parsers.parseStringToDouble;
import static com.ibosng.validationservice.utils.ValidationHelpers.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class MitarbeiterArbeitszeitenValidation extends AbstractValidation<VertragsdatenDto, Vertragsdaten> {

    @Override
    public boolean shouldValidationRun() {
        return getSources().isEmpty();
    }

    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final ArbeitszeitenService arbeitszeitenService;
    private final GlobalUserHolder globalUserHolder;

    @Override
    public boolean executeValidation(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (!isNullOrBlank(vertragsdatenDto.getWochenstunden())) {
            if (isDouble(vertragsdatenDto.getWochenstunden())) {
                Double wochenstunden = parseStringToDouble(vertragsdatenDto.getWochenstunden());
                if (wochenstunden <= 38 && wochenstunden > 0) {
                    ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
                    if (arbeitszeitenInfo == null) {
                        arbeitszeitenInfo = arbeitszeitenInfoService.createNewArbeitszeitInfo(vertragsdaten, globalUserHolder.getUsername());
                    }
                    List<Arbeitszeiten> arbeitszeitens = arbeitszeitenService.findByArbAndArbeitszeitenInfoId(arbeitszeitenInfo.getId());

                    if (arbeitszeitens.isEmpty()) {
                        arbeitszeitens = new ArrayList<>();
                        arbeitszeitens.add(createNewArbeitzeiten(arbeitszeitenInfo, globalUserHolder.getUsername()));
                        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell()) && vertragsdatenDto.getArbeitszeitmodell().toLowerCase().startsWith("gleitzeit") && vertragsdatenDto.isKernzeit()) {
                            Arbeitszeiten kernArbeitszeiten = createNewArbeitzeiten(arbeitszeitenInfo, globalUserHolder.getUsername());
                            kernArbeitszeiten.setKernzeit(true);
                            arbeitszeitens.add(kernArbeitszeiten);
                        }
                    } else {
                        List<Arbeitszeiten> kernArbeitszeitens = arbeitszeitens.stream().filter(ar -> Boolean.TRUE.equals(ar.getKernzeit())).toList();
                        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell()) && vertragsdatenDto.getArbeitszeitmodell().toLowerCase().startsWith("gleitzeit")) {
                            kernArbeitszeitens.forEach(del -> arbeitszeitenService.deleteById(del.getId()));
                            arbeitszeitens.removeAll(kernArbeitszeitens);
                        }
                        kernArbeitszeitens = arbeitszeitens.stream().filter(ar -> Boolean.TRUE.equals(ar.getKernzeit())).toList();
                        if (vertragsdatenDto.isKernzeit() && kernArbeitszeitens.isEmpty()) {
                            Arbeitszeiten kernArbeitszeiten = createNewArbeitzeiten(arbeitszeitenInfo, globalUserHolder.getUsername());
                            kernArbeitszeiten.setKernzeit(true);
                            arbeitszeitens.add(kernArbeitszeiten);
                        }
                    }
                    if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell())
                            && vertragsdatenDto.getArbeitszeitmodell().toLowerCase().startsWith("durchrechnung")
                            && isNullOrBlank(vertragsdatenDto.getAuswahlBegruendungFuerDurchrechner())) {
                        vertragsdaten.addError("auswahlBegruendungFuerDurchrechner", "Das Feld ist leer", globalUserHolder.getUsername());
                    }
                    for (Arbeitszeiten arbeitszeiten : arbeitszeitens) {
                        arbeitszeiten.setKernzeit(vertragsdatenDto.isKernzeit());
                        if (Boolean.TRUE.equals(arbeitszeiten.getKernzeit())) {
                            checkTotalHours(arbeitszeiten, vertragsdatenDto, vertragsdaten);

                            checkKernzeitArbeitszeiten(arbeitszeiten, vertragsdatenDto, vertragsdaten);
                        } else {
                            checkTotalHours(arbeitszeiten, vertragsdatenDto, vertragsdaten);
                            checkNormaleArbeitszeiten(arbeitszeiten, vertragsdatenDto, vertragsdaten);
                        }
                    }
                    arbeitszeitens.forEach(arbeitszeiten -> {
                        arbeitszeiten.setChangedBy(VALIDATION_SERVICE);
                        arbeitszeiten.setChangedOn(getLocalDateNow());
                    });
                    arbeitszeitenService.saveAll(arbeitszeitens);
                    return vertragsdaten.getErrors().isEmpty();
                }
                checkIfFieldsAreEmpty(vertragsdatenDto, vertragsdaten);
                return false;
            }
            checkIfFieldsAreEmpty(vertragsdatenDto, vertragsdaten);
            return false;
        }
        checkIfFieldsAreEmpty(vertragsdatenDto, vertragsdaten);
        return false;
    }

    @Deprecated(forRemoval = true)
    private double calculateTotalWorkingHours(Arbeitszeiten arbeitszeiten) {
        double totalHours = 0;
        if (arbeitszeiten.getMontagVon() != null && arbeitszeiten.getMontagBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getMontagVon().toString(), arbeitszeiten.getMontagBis().toString());
        }
        if (arbeitszeiten.getDienstagVon() != null && arbeitszeiten.getDienstagBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getDienstagVon().toString(), arbeitszeiten.getDienstagBis().toString());
        }
        if (arbeitszeiten.getMittwochVon() != null && arbeitszeiten.getMittwochBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getMittwochVon().toString(), arbeitszeiten.getMittwochBis().toString());
        }
        if (arbeitszeiten.getDonnerstagVon() != null && arbeitszeiten.getDonnerstagBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getDonnerstagVon().toString(), arbeitszeiten.getDonnerstagBis().toString());
        }
        if (arbeitszeiten.getFreitagVon() != null && arbeitszeiten.getFreitagBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getFreitagVon().toString(), arbeitszeiten.getFreitagBis().toString());
        }
        if (arbeitszeiten.getSamstagVon() != null && arbeitszeiten.getSamstagBis() != null) {
            totalHours += checkAndCalculateHours(arbeitszeiten.getSamstagVon().toString(), arbeitszeiten.getSamstagBis().toString());
        }

        return totalHours;
    }


    private boolean isErrorInTheList(List<VertragsdatenDataStatus> errors, String errorName) {
        return !errors.stream().filter(datastatus -> datastatus.getError().equals(errorName)).toList().isEmpty();
    }

    private void checkNormaleArbeitszeiten(Arbeitszeiten arbeitszeiten, VertragsdatenDto
            vertragsdatenDto, Vertragsdaten vertragsdaten) {
        boolean areMondayToFridayFilled = true;
        if (isValidTime(vertragsdatenDto.getAMontagVon()) && isDateInWorkingHours(vertragsdatenDto.getAMontagVon())) {
            arbeitszeiten.setMontagVon(parseTime(vertragsdatenDto.getAMontagVon()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMontagNetto())) {
            vertragsdaten.addError("amontagVon", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isValidTime(vertragsdatenDto.getAMontagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getAMontagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "amontagVon") &&
                parseTime(vertragsdatenDto.getAMontagBis()).isAfter(parseTime(vertragsdatenDto.getAMontagVon()))) {
            arbeitszeiten.setMontagBis(parseTime(vertragsdatenDto.getAMontagBis()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMontagNetto())) {
            vertragsdaten.addError("amontagBis", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isDouble(vertragsdatenDto.getAMontagNetto())) {
            arbeitszeiten.setMontagNetto(parseStringToDouble(vertragsdatenDto.getAMontagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMontagNetto())) {
            vertragsdaten.addError("amontagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setMontagVon(null);
            arbeitszeiten.setMontagBis(null);
            arbeitszeiten.setMontagNetto(null);
        }
        if (isValidTime(vertragsdatenDto.getADienstagVon()) && isDateInWorkingHours(vertragsdatenDto.getADienstagVon())) {
            arbeitszeiten.setDienstagVon(parseTime(vertragsdatenDto.getADienstagVon()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADienstagNetto())) {
            vertragsdaten.addError("adienstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isValidTime(vertragsdatenDto.getADienstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getADienstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "adienstagVon") &&
                parseTime(vertragsdatenDto.getADienstagBis()).isAfter(parseTime(vertragsdatenDto.getADienstagVon()))) {
            arbeitszeiten.setDienstagBis(parseTime(vertragsdatenDto.getADienstagBis()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADienstagNetto())) {
            vertragsdaten.addError("adienstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isDouble(vertragsdatenDto.getADienstagNetto())) {
            arbeitszeiten.setDienstagNetto(parseStringToDouble(vertragsdatenDto.getADienstagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADienstagNetto())) {
            vertragsdaten.addError("adienstagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setDienstagVon(null);
            arbeitszeiten.setDienstagBis(null);
            arbeitszeiten.setDienstagNetto(null);
        }
        if (isValidTime(vertragsdatenDto.getAMittwochVon()) && isDateInWorkingHours(vertragsdatenDto.getAMittwochVon())) {
            arbeitszeiten.setMittwochVon(parseTime(vertragsdatenDto.getAMittwochVon()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMittwochNetto())) {
            vertragsdaten.addError("amittwochVon", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isValidTime(vertragsdatenDto.getAMittwochBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getAMittwochBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "amittwochVon") &&
                parseTime(vertragsdatenDto.getAMittwochBis()).isAfter(parseTime(vertragsdatenDto.getAMittwochVon()))) {
            arbeitszeiten.setMittwochBis(parseTime(vertragsdatenDto.getAMittwochBis()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMittwochNetto())) {
            vertragsdaten.addError("amittwochBis", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isDouble(vertragsdatenDto.getAMittwochNetto())) {
            arbeitszeiten.setMittwochNetto(parseStringToDouble(vertragsdatenDto.getAMittwochNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAMittwochNetto())) {
            vertragsdaten.addError("amittwochNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setMittwochVon(null);
            arbeitszeiten.setMittwochBis(null);
            arbeitszeiten.setMittwochNetto(null);
        }
        if (isValidTime(vertragsdatenDto.getADonnerstagVon()) && isDateInWorkingHours(vertragsdatenDto.getADonnerstagVon())) {
            arbeitszeiten.setDonnerstagVon(parseTime(vertragsdatenDto.getADonnerstagVon()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADonnerstagNetto())) {
            vertragsdaten.addError("adonnerstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isValidTime(vertragsdatenDto.getADonnerstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getADonnerstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "adonnerstagVon") &&
                parseTime(vertragsdatenDto.getADonnerstagBis()).isAfter(parseTime(vertragsdatenDto.getADonnerstagVon()))) {
            arbeitszeiten.setDonnerstagBis(parseTime(vertragsdatenDto.getADonnerstagBis()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADonnerstagNetto())) {
            vertragsdaten.addError("adonnerstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isDouble(vertragsdatenDto.getADonnerstagNetto())) {
            arbeitszeiten.setDonnerstagNetto(parseStringToDouble(vertragsdatenDto.getADonnerstagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getADonnerstagNetto())) {
            vertragsdaten.addError("adonnerstagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setDonnerstagVon(null);
            arbeitszeiten.setDonnerstagBis(null);
            arbeitszeiten.setDonnerstagNetto(null);
        }
        if (isValidTime(vertragsdatenDto.getAFreitagVon()) && isDateInWorkingHours(vertragsdatenDto.getAFreitagVon())) {
            arbeitszeiten.setFreitagVon(parseTime(vertragsdatenDto.getAFreitagVon()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAFreitagNetto())) {
            vertragsdaten.addError("afreitagVon", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isValidTime(vertragsdatenDto.getAFreitagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getAFreitagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "afreitagVon") &&
                parseTime(vertragsdatenDto.getAFreitagBis()).isAfter(parseTime(vertragsdatenDto.getAFreitagVon()))) {
            arbeitszeiten.setFreitagBis(parseTime(vertragsdatenDto.getAFreitagBis()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAFreitagNetto())) {
            vertragsdaten.addError("afreitagBis", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        }
        if (isDouble(vertragsdatenDto.getAFreitagNetto())) {
            arbeitszeiten.setFreitagNetto(parseStringToDouble(vertragsdatenDto.getAFreitagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getAFreitagNetto())) {
            vertragsdaten.addError("afreitagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setFreitagVon(null);
            arbeitszeiten.setFreitagBis(null);
            arbeitszeiten.setFreitagNetto(null);
        }

        if (isValidTime(vertragsdatenDto.getASamstagVon()) && isDateInWorkingHours(vertragsdatenDto.getASamstagVon())) {
            arbeitszeiten.setSamstagVon(parseTime(vertragsdatenDto.getASamstagVon()));
        } else if (!areMondayToFridayFilled && !isNullOrBalnkOrZero(vertragsdatenDto.getASamstagNetto())) {
            vertragsdaten.addError("asamstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }
        if (isValidTime(vertragsdatenDto.getASamstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getASamstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "asamstagVon") &&
                parseTime(vertragsdatenDto.getASamstagBis()).isAfter(parseTime(vertragsdatenDto.getASamstagVon()))) {
            arbeitszeiten.setSamstagBis(parseTime(vertragsdatenDto.getASamstagBis()));
        } else if (!areMondayToFridayFilled && !isNullOrBalnkOrZero(vertragsdatenDto.getASamstagNetto())) {
            vertragsdaten.addError("asamstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }
        if (isDouble(vertragsdatenDto.getASamstagNetto())) {
            arbeitszeiten.setSamstagNetto(parseStringToDouble(vertragsdatenDto.getASamstagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getASamstagNetto())) {
            vertragsdaten.addError("asamstagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setSamstagVon(null);
            arbeitszeiten.setSamstagBis(null);
            arbeitszeiten.setSamstagNetto(null);
        }

        if (isValidTime(vertragsdatenDto.getASonntagVon()) && isDateInWorkingHours(vertragsdatenDto.getASonntagVon())) {
            arbeitszeiten.setSonntagVon(parseTime(vertragsdatenDto.getASonntagVon()));
        } else if (!areMondayToFridayFilled && !isNullOrBalnkOrZero(vertragsdatenDto.getASonntagNetto())) {
            vertragsdaten.addError("asonntagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }
        if (isValidTime(vertragsdatenDto.getASonntagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getASonntagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "asonntagVon") &&
                parseTime(vertragsdatenDto.getASonntagBis()).isAfter(parseTime(vertragsdatenDto.getASonntagVon()))) {
            arbeitszeiten.setSonntagBis(parseTime(vertragsdatenDto.getASonntagBis()));
        } else if (!areMondayToFridayFilled && !isNullOrBalnkOrZero(vertragsdatenDto.getASonntagNetto())) {
            vertragsdaten.addError("asonntagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }
        if (isDouble(vertragsdatenDto.getASonntagNetto())) {
            arbeitszeiten.setSonntagNetto(parseStringToDouble(vertragsdatenDto.getASonntagNetto()));
        } else if (!isNullOrBalnkOrZero(vertragsdatenDto.getASonntagNetto())) {
            vertragsdaten.addError("asonntagNetto", "Ungültiges Feld", globalUserHolder.getUsername());
            areMondayToFridayFilled = false;
        } else {
            arbeitszeiten.setSonntagVon(null);
            arbeitszeiten.setSonntagBis(null);
            arbeitszeiten.setSonntagNetto(null);
        }
    }

    private void checkMaxHoursPerDay(VertragsdatenDto vertragsdatenDto, String von, String bis, Vertragsdaten vertragsdaten, String tag, double tagNetto) {
        // No computation possible if von/bis is missing
        if (isNullOrBlank(von) || isNullOrBlank(bis)) {
            return;
        }

        // Gleitzteit max is 12h/day
        if (!isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell())) {
            if (vertragsdatenDto.getArbeitszeitmodell().toLowerCase().startsWith("gleitzeit")) {
                if (tagNetto > 12) {
                    vertragsdaten.addError(tag, "Die maximale Arbeitszeit für Gleitzeit liegt bei 12h/Tag", globalUserHolder.getUsername());
                }
            } else {
                if (tagNetto > 10) {
                    vertragsdaten.addError(tag, "Die maximale Arbeitszeit für " + vertragsdatenDto.getArbeitszeitmodell() + " liegt bei 10h/Tag", globalUserHolder.getUsername());
                }
            }
        }
    }

    private void checkTotalHours(Arbeitszeiten arbeitszeiten, VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        double summeNetto = 0;

        // Montag
        if (!isNullOrBlank(vertragsdatenDto.getAMontagNetto())) {
            double nettoMontag = Double.parseDouble(vertragsdatenDto.getAMontagNetto());
            summeNetto += nettoMontag;
            arbeitszeiten.setMontagNetto(nettoMontag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoMontag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getAMontagVon())) {
                    vertragsdaten.addError("amontagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getAMontagBis())) {
                    vertragsdaten.addError("amontagBis", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getAMontagVon(), vertragsdatenDto.getAMontagBis(), vertragsdaten, "amontagNetto", nettoMontag);
            }
        }

        // Dienstag
        if (!isNullOrBlank(vertragsdatenDto.getADienstagNetto())) {
            double nettoDienstag = Double.parseDouble(vertragsdatenDto.getADienstagNetto());
            summeNetto += nettoDienstag;
            arbeitszeiten.setDienstagNetto(nettoDienstag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoDienstag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getADienstagVon())) {
                    vertragsdaten.addError("adienstagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getADienstagBis())) {
                    vertragsdaten.addError("adienstagBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getADienstagVon(), vertragsdatenDto.getADienstagBis(), vertragsdaten, "adienstagNetto", nettoDienstag);

                // Check if the time gap between Montag Bis and Dienstag Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getAMontagBis()) && !isNullOrBlank(vertragsdatenDto.getADienstagVon())) {
                    LocalDateTime montagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.MONDAY),
                            LocalTime.parse(vertragsdatenDto.getAMontagBis()));
                    LocalDateTime dienstagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.TUESDAY),
                            LocalTime.parse(vertragsdatenDto.getADienstagVon()));
                    if (ChronoUnit.HOURS.between(montagBis, dienstagVon) < 11) {
                        vertragsdaten.addError("adienstagVon", "Die Ruhezeit zwischen Montag Bis und Dienstag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Mittwoch
        if (!isNullOrBlank(vertragsdatenDto.getAMittwochNetto())) {
            double nettoMittwoch = Double.parseDouble(vertragsdatenDto.getAMittwochNetto());
            summeNetto += nettoMittwoch;
            arbeitszeiten.setMittwochNetto(nettoMittwoch);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoMittwoch != 0) {
                if (isNullOrBlank(vertragsdatenDto.getAMittwochVon())) {
                    vertragsdaten.addError("amittwochVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getAMittwochBis())) {
                    vertragsdaten.addError("amittwochBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getAMittwochVon(), vertragsdatenDto.getAMittwochBis(), vertragsdaten, "amittwochNetto", nettoMittwoch);
                // Check if the time gap between Dienstag Bis and Mittwoch Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getADienstagBis()) && !isNullOrBlank(vertragsdatenDto.getAMittwochVon())) {
                    LocalDateTime dienstagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.TUESDAY),
                            LocalTime.parse(vertragsdatenDto.getADienstagBis()));
                    LocalDateTime mittwochVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY),
                            LocalTime.parse(vertragsdatenDto.getAMittwochVon()));
                    if (ChronoUnit.HOURS.between(dienstagBis, mittwochVon) < 11) {
                        vertragsdaten.addError("amittwochVon", "Die Ruhezeit zwischen Dienstag Bis und Mittwoch Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Donnerstag
        if (!isNullOrBlank(vertragsdatenDto.getADonnerstagNetto())) {
            double nettoDonnerstag = Double.parseDouble(vertragsdatenDto.getADonnerstagNetto());
            summeNetto += nettoDonnerstag;
            arbeitszeiten.setDonnerstagNetto(nettoDonnerstag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoDonnerstag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getADonnerstagVon())) {
                    vertragsdaten.addError("adonnerstagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getADonnerstagBis())) {
                    vertragsdaten.addError("adonnerstagBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getADonnerstagVon(), vertragsdatenDto.getADonnerstagBis(), vertragsdaten, "adonnerstagNetto", nettoDonnerstag);
                // Check if the time gap between Mittwoch Bis and Donnerstag Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getAMittwochBis()) && !isNullOrBlank(vertragsdatenDto.getADonnerstagVon())) {
                    LocalDateTime mittwochBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.WEDNESDAY),
                            LocalTime.parse(vertragsdatenDto.getAMittwochBis()));
                    LocalDateTime donnerstagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.THURSDAY),
                            LocalTime.parse(vertragsdatenDto.getADonnerstagVon()));
                    if (ChronoUnit.HOURS.between(mittwochBis, donnerstagVon) < 11) {
                        vertragsdaten.addError("adonnerstagVon", "Die Ruhezeit zwischen Mittwoch Bis und Donnerstag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Freitag
        if (!isNullOrBlank(vertragsdatenDto.getAFreitagNetto())) {
            double nettoFreitag = Double.parseDouble(vertragsdatenDto.getAFreitagNetto());
            summeNetto += nettoFreitag;
            arbeitszeiten.setFreitagNetto(nettoFreitag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoFreitag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getAFreitagVon())) {
                    vertragsdaten.addError("afreitagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getAFreitagBis())) {
                    vertragsdaten.addError("afreitagBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getAFreitagVon(), vertragsdatenDto.getAFreitagBis(), vertragsdaten, "afreitagNetto", nettoFreitag);
                // Check if the time gap between Donnerstag Bis and Freitag Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getADonnerstagBis()) && !isNullOrBlank(vertragsdatenDto.getAFreitagVon())) {
                    LocalDateTime donnerstagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.THURSDAY),
                            LocalTime.parse(vertragsdatenDto.getADonnerstagBis()));
                    LocalDateTime freitagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.FRIDAY),
                            LocalTime.parse(vertragsdatenDto.getAFreitagVon()));
                    if (ChronoUnit.HOURS.between(donnerstagBis, freitagVon) < 11) {
                        vertragsdaten.addError("afreitagVon", "Die Ruhezeit zwischen Donnerstag Bis und Freitag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Samstag
        if (!isNullOrBlank(vertragsdatenDto.getASamstagNetto())) {
            double nettoSamstag = Double.parseDouble(vertragsdatenDto.getASamstagNetto());
            summeNetto += nettoSamstag;
            arbeitszeiten.setSamstagNetto(nettoSamstag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoSamstag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getASamstagVon())) {
                    vertragsdaten.addError("asamstagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getASamstagBis())) {
                    vertragsdaten.addError("asamstagBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getASamstagVon(), vertragsdatenDto.getASamstagBis(), vertragsdaten, "asamstagNetto", nettoSamstag);
                // Check if the time gap between Freitag Bis and Samstag Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getAFreitagBis()) && !isNullOrBlank(vertragsdatenDto.getASamstagVon())) {
                    LocalDateTime freitagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.FRIDAY),
                            LocalTime.parse(vertragsdatenDto.getAFreitagBis()));
                    LocalDateTime samstagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.SATURDAY),
                            LocalTime.parse(vertragsdatenDto.getASamstagVon()));
                    if (ChronoUnit.HOURS.between(freitagBis, samstagVon) < 11) {
                        vertragsdaten.addError("asamstagVon", "Die Ruhezeit zwischen Freitag Bis und Samstag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Sonntag
        if (!isNullOrBlank(vertragsdatenDto.getASonntagNetto())) {
            double nettoSonntag = Double.parseDouble(vertragsdatenDto.getASonntagNetto());
            summeNetto += nettoSonntag;
            arbeitszeiten.setSonntagNetto(nettoSonntag);
            // Ensure von and bis are set if Nettostunden != 0 for that day
            if (nettoSonntag != 0) {
                if (isNullOrBlank(vertragsdatenDto.getASonntagVon())) {
                    vertragsdaten.addError("asonntagVon", "Von nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                if (isNullOrBlank(vertragsdatenDto.getASonntagBis())) {
                    vertragsdaten.addError("asonntagBis", "Bis nicht gesetzt bei Netto verschieden von 0", globalUserHolder.getUsername());
                }
                // Check max hours per day
                checkMaxHoursPerDay(vertragsdatenDto, vertragsdatenDto.getASonntagVon(), vertragsdatenDto.getASonntagBis(), vertragsdaten, "asonntagNetto", nettoSonntag);
                // Check if the time gap between Samstag Bis and Sonntag Von is at least 11 hours
                if (!isNullOrBlank(vertragsdatenDto.getASamstagBis()) && !isNullOrBlank(vertragsdatenDto.getASonntagVon())) {
                    LocalDateTime samstagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.SATURDAY),
                            LocalTime.parse(vertragsdatenDto.getASamstagBis()));
                    LocalDateTime sonntagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),
                            LocalTime.parse(vertragsdatenDto.getASonntagVon()));
                    if (ChronoUnit.HOURS.between(samstagBis, sonntagVon) < 11) {
                        vertragsdaten.addError("asonntagVon", "Die Ruhezeit zwischen Samstag Bis und Sonntag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }

                if (!isNullOrBlank(vertragsdatenDto.getASonntagBis()) && !isNullOrBlank(vertragsdatenDto.getAMontagVon())) {
                    LocalDateTime sonntagBis = LocalDateTime.of(LocalDate.now().with(DayOfWeek.SUNDAY),
                            LocalTime.parse(vertragsdatenDto.getASonntagBis()));
                    LocalDateTime montagVon = LocalDateTime.of(LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1),
                            LocalTime.parse(vertragsdatenDto.getAMontagVon()));
                    if (ChronoUnit.HOURS.between(sonntagBis, montagVon) < 11) {
                        vertragsdaten.addError("amontagVon", "Die Ruhezeit zwischen Sonntag Bis und Montag Von ist weniger als 11 Stunden", globalUserHolder.getUsername());
                    }
                }
            }
        }

        // Check if the Sum of Nettostuden is equal to the Sum of Wochenstunden
        double summeWochenstunden = Double.parseDouble(vertragsdatenDto.getWochenstunden());
        if (summeNetto != summeWochenstunden) {
            if (summeNetto > summeWochenstunden) {
                vertragsdaten.addError("wochenstunden", "Es wurde(n) " + (summeNetto - summeWochenstunden) + " Stunden zu viel verteilt.", globalUserHolder.getUsername());
            } else {
                vertragsdaten.addError("wochenstunden", (summeWochenstunden - summeNetto) + " Stunde(n) müssen noch verteilt werden.", globalUserHolder.getUsername());
            }
        }
    }

    private void checkKernzeitArbeitszeiten(Arbeitszeiten arbeitszeiten, VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (isNullOrBlank(vertragsdatenDto.getKMontagVon())) {
            arbeitszeiten.setMontagVon(null);
        } else if (isValidTime(vertragsdatenDto.getKMontagVon()) && isDateInWorkingHours(vertragsdatenDto.getKMontagVon())) {
            arbeitszeiten.setMontagVon(parseTime(vertragsdatenDto.getKMontagVon()));
        } else {
            vertragsdaten.addError("kmontagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKMontagBis())) {
            arbeitszeiten.setMontagBis(null);
        } else if (isValidTime(vertragsdatenDto.getKMontagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKMontagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "kMontagVon") &&
                !isNullOrBlank(vertragsdatenDto.getKMontagVon()) &&
                parseTime(vertragsdatenDto.getKMontagBis()).isAfter(parseTime(vertragsdatenDto.getKMontagVon()))) {
            arbeitszeiten.setMontagBis(parseTime(vertragsdatenDto.getKMontagBis()));
        } else {
            vertragsdaten.addError("kmontagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKDienstagVon())) {
            arbeitszeiten.setDienstagVon(null);
        } else if (isValidTime(vertragsdatenDto.getKDienstagVon()) && isDateInWorkingHours(vertragsdatenDto.getKDienstagVon())) {
            arbeitszeiten.setDienstagVon(parseTime(vertragsdatenDto.getKDienstagVon()));
        } else {
            vertragsdaten.addError("kdienstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKDienstagBis())) {
            arbeitszeiten.setDienstagBis(null);
        } else if (isValidTime(vertragsdatenDto.getKDienstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKDienstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "kdienstagVon") &&
                !isNullOrBlank(vertragsdatenDto.getKDienstagVon()) &&
                parseTime(vertragsdatenDto.getKDienstagBis()).isAfter(parseTime(vertragsdatenDto.getKDienstagVon()))) {
            arbeitszeiten.setDienstagBis(parseTime(vertragsdatenDto.getKDienstagBis()));
        } else {
            vertragsdaten.addError("kdienstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKMittwochVon())) {
            arbeitszeiten.setMittwochVon(null);
        } else if (isValidTime(vertragsdatenDto.getKMittwochVon()) && isDateInWorkingHours(vertragsdatenDto.getKMittwochVon())) {
            arbeitszeiten.setMittwochVon(parseTime(vertragsdatenDto.getKMittwochVon()));
        } else {
            vertragsdaten.addError("kmittwochVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKMittwochBis())) {
            arbeitszeiten.setMittwochBis(null);
        } else if (isValidTime(vertragsdatenDto.getKMittwochBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKMittwochBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "kmittwochVon") &&
                !isNullOrBlank(vertragsdatenDto.getKMittwochVon()) &&
                parseTime(vertragsdatenDto.getKMittwochBis()).isAfter(parseTime(vertragsdatenDto.getKMittwochVon()))) {
            arbeitszeiten.setMittwochBis(parseTime(vertragsdatenDto.getKMittwochBis()));
        } else {
            vertragsdaten.addError("kmittwochBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKDonnerstagVon())) {
            arbeitszeiten.setDonnerstagVon(null);
        } else if (isValidTime(vertragsdatenDto.getKDonnerstagVon()) && isDateInWorkingHours(vertragsdatenDto.getKDonnerstagVon())) {
            arbeitszeiten.setDonnerstagVon(parseTime(vertragsdatenDto.getKDonnerstagVon()));
        } else {
            vertragsdaten.addError("kdonnerstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKDonnerstagBis())) {
            arbeitszeiten.setDonnerstagBis(null);
        } else if (isValidTime(vertragsdatenDto.getKDonnerstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKDonnerstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "kdonnerstagVon") &&
                !isNullOrBlank(vertragsdatenDto.getKDonnerstagVon()) &&
                parseTime(vertragsdatenDto.getKDonnerstagBis()).isAfter(parseTime(vertragsdatenDto.getKDonnerstagVon()))) {
            arbeitszeiten.setDonnerstagBis(parseTime(vertragsdatenDto.getKDonnerstagBis()));
        } else {
            vertragsdaten.addError("kdonnerstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKFreitagVon())) {
            arbeitszeiten.setFreitagVon(null);
        } else if (isValidTime(vertragsdatenDto.getKFreitagVon()) && isDateInWorkingHours(vertragsdatenDto.getKFreitagVon())) {
            arbeitszeiten.setFreitagVon(parseTime(vertragsdatenDto.getKFreitagVon()));
        } else {
            vertragsdaten.addError("kfreitagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKFreitagBis())) {
            arbeitszeiten.setFreitagBis(null);
        } else if (isValidTime(vertragsdatenDto.getKFreitagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKFreitagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "kfreitagVon") &&
                !isNullOrBlank(vertragsdatenDto.getKFreitagVon()) &&
                parseTime(vertragsdatenDto.getKFreitagBis()).isAfter(parseTime(vertragsdatenDto.getKFreitagVon()))) {
            arbeitszeiten.setFreitagBis(parseTime(vertragsdatenDto.getKFreitagBis()));
        } else {
            vertragsdaten.addError("kfreitagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKSamstagVon())) {
            arbeitszeiten.setSamstagVon(null);
        } else if (isValidTime(vertragsdatenDto.getKSamstagVon()) && isDateInWorkingHours(vertragsdatenDto.getKSamstagVon())) {
            arbeitszeiten.setSamstagVon(parseTime(vertragsdatenDto.getKSamstagVon()));
        } else {
            vertragsdaten.addError("ksamstagVon", "Ungültiges Feld", globalUserHolder.getUsername());
        }

        if (isNullOrBlank(vertragsdatenDto.getKSamstagBis())) {
            arbeitszeiten.setSamstagBis(null);
        } else if (isValidTime(vertragsdatenDto.getKSamstagBis()) &&
                isDateInWorkingHours(vertragsdatenDto.getKSamstagBis()) &&
                !isErrorInTheList(vertragsdaten.getErrors(), "ksamstagVon") &&
                !isNullOrBlank(vertragsdatenDto.getKSamstagVon()) &&
                parseTime(vertragsdatenDto.getKSamstagBis()).isAfter(parseTime(vertragsdatenDto.getKSamstagVon()))) {
            arbeitszeiten.setSamstagBis(parseTime(vertragsdatenDto.getKSamstagBis()));
        } else {
            vertragsdaten.addError("ksamstagBis", "Ungültiges Feld", globalUserHolder.getUsername());
        }
    }


    public double calculateTotalWorkingHoursDurchrechnung(VertragsdatenDto vertragsdatenDto) {
        double totalHours = 0;
        totalHours += checkAndCalculateHours(vertragsdatenDto.getAMontagVon(), vertragsdatenDto.getAMontagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getADienstagVon(), vertragsdatenDto.getADienstagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getAMittwochVon(), vertragsdatenDto.getAMittwochBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getADonnerstagVon(), vertragsdatenDto.getADonnerstagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getAFreitagVon(), vertragsdatenDto.getAFreitagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getASamstagVon(), vertragsdatenDto.getASamstagBis());

        return totalHours;
    }
/*    public double calculateTotalWorkingHours(Arbeitszeiten arbeitszeiten) {
        double totalHours = 0;
        totalHours += checkAndCalculateHours(arbeitszeiten.getMontagVon(), arbeitszeiten.getMontagBis());
        totalHours += checkAndCalculateHours(arbeitszeiten.getDienstagVon(), arbeitszeiten.getDienstagBis());
        totalHours += checkAndCalculateHours(arbeitszeiten.getMittwochVon(), arbeitszeiten.getMittwochBis());
        totalHours += checkAndCalculateHours(arbeitszeiten.getDonnerstagVon(), arbeitszeiten.getDonnerstagBis());
        totalHours += checkAndCalculateHours(arbeitszeiten.getFreitagVon(), arbeitszeiten.getFreitagBis());
        totalHours += checkAndCalculateHours(arbeitszeiten.getSamstagVon(), arbeitszeiten.getSamstagBis());

        return totalHours;
    }*/


    public double calculateTotalWorkingHoursGleitzeit(VertragsdatenDto vertragsdatenDto) {
        double totalHours = 0;
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKMontagVon(), vertragsdatenDto.getKMontagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKDienstagVon(), vertragsdatenDto.getKDienstagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKMittwochVon(), vertragsdatenDto.getKMittwochBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKDonnerstagVon(), vertragsdatenDto.getKDonnerstagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKFreitagVon(), vertragsdatenDto.getKFreitagBis());
        totalHours += checkAndCalculateHours(vertragsdatenDto.getKSamstagVon(), vertragsdatenDto.getKSamstagBis());

        return totalHours;
    }

    private double checkAndCalculateHours(String startTimeStr, String endTimeStr) {
        if (isValidTime(startTimeStr) && isValidTime(endTimeStr) && isDateInWorkingHours(startTimeStr) && isDateInWorkingHours(endTimeStr)) {
            LocalTime startTime = parseTime(startTimeStr);
            LocalTime endTime = parseTime(endTimeStr);
            return calculateHoursBetween(startTime, endTime);
        } else {
            return 0;
        }
    }

    private double calculateHoursBetween(LocalTime startTime, LocalTime endTime) {
        return ChronoUnit.MINUTES.between(startTime, endTime) / 60.0;
    }

    private void checkIfFieldsAreEmpty(VertragsdatenDto vertragsdatenDto, Vertragsdaten vertragsdaten) {
        if (isNullOrBlank(vertragsdatenDto.getBeschaeftigungsausmass())) {
            vertragsdaten.addError("beschaeftigungsausmass", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getBeschaeftigungsstatus())) {
            vertragsdaten.addError("beschaeftigungsstatus", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getArbeitszeitmodell())) {
            vertragsdaten.addError("arbeitszeitmodell", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMontagVon())) {
            vertragsdaten.addError("amontagVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMontagBis())) {
            vertragsdaten.addError("amontagBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMontagNetto())) {
            vertragsdaten.addError("amontagNetto", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADienstagVon())) {
            vertragsdaten.addError("adienstagVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADienstagBis())) {
            vertragsdaten.addError("adienstagBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADienstagNetto())) {
            vertragsdaten.addError("adienstagNetto", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMittwochVon())) {
            vertragsdaten.addError("amittwochVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMittwochBis())) {
            vertragsdaten.addError("amittwochBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAMittwochNetto())) {
            vertragsdaten.addError("amittwochNetto", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADonnerstagVon())) {
            vertragsdaten.addError("adonnerstagVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADonnerstagBis())) {
            vertragsdaten.addError("adonnerstagBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getADonnerstagNetto())) {
            vertragsdaten.addError("adonnerstagNetto", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAFreitagVon())) {
            vertragsdaten.addError("afreitagVon", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAFreitagBis())) {
            vertragsdaten.addError("afreitagBis", "Das Feld ist leer", globalUserHolder.getUsername());
        }
        if (isNullOrBlank(vertragsdatenDto.getAFreitagNetto())) {
            vertragsdaten.addError("afreitagNetto", "Das Feld ist leer", globalUserHolder.getUsername());
        }
    }


}

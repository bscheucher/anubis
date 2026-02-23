package com.ibosng.validationservice.services.impl;

import com.ibosng._config.GlobalUserHolder;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Gehalt;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.services.masterdata.GehaltService;
import com.ibosng.dbservice.services.masterdata.KVStufeService;
import com.ibosng.dbservice.services.masterdata.VertragsartService;
import com.ibosng.dbservice.services.mitarbeiter.*;
import com.ibosng.validationservice.services.KVEinstufungService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.validationservice.utils.Constants.*;
import static com.ibosng.validationservice.utils.Parsers.parseStringToBigDecimal;

@Service
@Slf4j
public class KVEinstufungServiceImpl implements KVEinstufungService {
    private static final LocalDate START_DATE_PHASE_1 = LocalDate.of(2025, 5, 1);
    private static final LocalDate END_DATE_PHASE_1 = LocalDate.of(2028, 4, 30);
    private static final LocalDate START_DATE_PHASE_2 = LocalDate.of(2028, 5, 1);
    private static final LocalDate END_DATE_PHASE_2 = LocalDate.of(2029, 4, 30);
    private static final LocalDate START_DATE_PHASE_3 = LocalDate.of(2029, 5, 1);
    private final ManageWFItemsService manageWFItemsService;
    private final VordienstzeitenService vordienstzeitenService;
    private final VertragsdatenService vertragsdatenService;
    private final KVStufeService kvStufeService;
    private final VertragsartService vertragsartService;
    private final GehaltInfoService gehaltInfoService;
    private final GehaltService gehaltService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final GehaltInfoZulageService gehaltInfoZulageService;
    private final ManageWFsService manageWFsService;
    private final GlobalUserHolder globalUserHolder;
    private Double fullTimeHours;

    @Getter
    @Setter
    private WWorkflow workflow;

    @Getter
    @Setter
    private List<Vordienstzeiten> vordienstzeitens;

    @Getter
    @Setter
    private List<String> freeVertragsarten;

    @Getter
    @Setter
    private BigDecimal fixMonths;

    @Getter
    @Setter
    private BigDecimal freeMonths;

    // TODO - convert fields to local variables
    @Getter
    @Setter
    private Vertragsdaten vertragsdaten;

    public KVEinstufungServiceImpl(@Value("${kvCalculationFullTimeHours:38.0}") Double fullTimeHours,
                                   ManageWFItemsService manageWFItemsService,
                                   VordienstzeitenService vordienstzeitenService,
                                   VertragsdatenService vertragsdatenService,
                                   KVStufeService kvStufeService,
                                   VertragsartService vertragsartService,
                                   GehaltInfoService gehaltInfoService,
                                   GehaltService gehaltService,
                                   ArbeitszeitenInfoService arbeitszeitenInfoService,
                                   GehaltInfoZulageService gehaltInfoZulageService,
                                   ManageWFsService manageWFsService,
                                   GlobalUserHolder globalUserHolder) {
        this.fullTimeHours = fullTimeHours;
        this.manageWFItemsService = manageWFItemsService;
        this.vordienstzeitenService = vordienstzeitenService;
        this.vertragsdatenService = vertragsdatenService;
        this.kvStufeService = kvStufeService;
        this.vertragsartService = vertragsartService;
        this.gehaltInfoService = gehaltInfoService;
        this.gehaltService = gehaltService;
        this.arbeitszeitenInfoService = arbeitszeitenInfoService;
        this.gehaltInfoZulageService = gehaltInfoZulageService;
        this.manageWFsService = manageWFsService;
        this.globalUserHolder = globalUserHolder;
    }

    private Integer getInitialMaxMonths(LocalDate eintrittsdatum) {
        if ((eintrittsdatum.isAfter(START_DATE_PHASE_1) || eintrittsdatum.equals(START_DATE_PHASE_1)) && eintrittsdatum.isBefore(END_DATE_PHASE_1.plusDays(1))) {
            return 5 * MONTHS_IN_YEAR;
        } else if ((eintrittsdatum.isAfter(START_DATE_PHASE_2) || eintrittsdatum.equals(START_DATE_PHASE_2)) && eintrittsdatum.isBefore(END_DATE_PHASE_2.plusDays(1))) {
            return 7 * MONTHS_IN_YEAR;
        }
        return 8 * MONTHS_IN_YEAR;
    }

    private int calculateMaxMonths(LocalDate eintrittsdatum) {
        int maxMonths;
        if (eintrittsdatum.isBefore(START_DATE_PHASE_1)) {
            // unreachable - eintrittsdatum cannot be in the past
            maxMonths = getInitialMaxMonths(eintrittsdatum);
        } else if (eintrittsdatum.isBefore(END_DATE_PHASE_1.plusDays(1))) {
            int monthsSinceStart = (int) ChronoUnit.MONTHS.between(START_DATE_PHASE_1, eintrittsdatum) + 1;
            monthsSinceStart = Math.min(monthsSinceStart, 24);
            int initialMaxMonths = getInitialMaxMonths(eintrittsdatum);
            maxMonths = Math.min(initialMaxMonths + 2 * MONTHS_IN_YEAR, monthsSinceStart + initialMaxMonths);
        } else if (eintrittsdatum.isBefore(END_DATE_PHASE_2.plusDays(1))) {
            int monthsSinceStart = (int) ChronoUnit.MONTHS.between(START_DATE_PHASE_2, eintrittsdatum) + 1;
            int initialMaxMonths = getInitialMaxMonths(eintrittsdatum);
            maxMonths = Math.min(initialMaxMonths + MONTHS_IN_YEAR, monthsSinceStart + initialMaxMonths);
        } else {
            maxMonths = 96;
        }
        return maxMonths;
    }

    @Override
    public ResponseEntity<WWorkflowStatus> calculateKVEinstufung(String personalNummer, Boolean isOnboarding) {
        Vertragsdaten vertragsdatenForWorkflow;
        if (isOnboarding) {
            startWorkflowitem(personalNummer);
            vertragsdatenForWorkflow = vertragsdatenService.findByPersonalnummerStringAndStatus(personalNummer, List.of(MitarbeiterStatus.VALIDATED)).stream().findFirst().orElse(null);
        } else {
            vertragsdatenForWorkflow = vertragsdatenService.findByPersonalnummerStringAndStatus(personalNummer, List.of(MitarbeiterStatus.ACTIVE)).stream().findFirst().orElse(null);
        }
        setVertragsdaten(vertragsdatenForWorkflow);
        setVordienstzeiten(personalNummer);
        setFreeMonths(BigDecimal.valueOf(0));
        setFixMonths(BigDecimal.valueOf(0));
        setFreeVertragsarten();
        GehaltInfo gehaltInfo = findGehaltInfo(getVertragsdaten());
        int totalMonths;
        if (Boolean.TRUE.equals(gehaltInfo.getFacheinschlaegigeTaetigkeitenGeprueft())) {
            totalMonths = calculateTotalMonths(getVordienstzeitens());
        } else {
            totalMonths = 0;
        }
        if (gehaltInfo.getAngerechneteIbisMonate() != null) {
            totalMonths = totalMonths + gehaltInfo.getAngerechneteIbisMonate();
        }

        LocalDate nextStufeDate = null;
        if (getVertragsdaten().getEintritt() != null) {
            nextStufeDate = calculateNextStufeDate(getVertragsdaten().getEintritt(), totalMonths);
        } else {
            log.warn("No entry date for employee with personal nummer: {} , next KV-Stufe can not be calculated", personalNummer);
        }
        if (setGehaltInfoData(totalMonths, nextStufeDate, getVertragsdaten())) {
            if (isOnboarding) {
                manageWFItemsService.setWFItemStatus(getWorkflow(), SWorkflowItems.KV_EINSTUFUNG_BERECHNEN, WWorkflowStatus.COMPLETED, globalUserHolder.getUsername());
            }
            return ResponseEntity.ok(WWorkflowStatus.COMPLETED);
        } else {
            if (isOnboarding) {
                manageWFItemsService.setWFItemStatus(getWorkflow(), SWorkflowItems.VERTRAGSDATEN_ERFASSEN, WWorkflowStatus.ERROR, globalUserHolder.getUsername());
                manageWFItemsService.setWFItemStatus(getWorkflow(), SWorkflowItems.KV_EINSTUFUNG_BERECHNEN, WWorkflowStatus.ERROR, globalUserHolder.getUsername());
            }
            return ResponseEntity.ok(WWorkflowStatus.ERROR);
        }
    }

    private GehaltInfo findGehaltInfo(Vertragsdaten vertragsdaten) {
        return gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
    }

    private boolean setGehaltInfoData(int totalMonths, LocalDate nextStufeDate, Vertragsdaten vertragsdaten) {
        GehaltInfo gehaltInfo = findGehaltInfo(vertragsdaten);
        BigDecimal zulagenTotal = new BigDecimal(0);
        // Add all Zulagen to GesamtBrutto
        List<GehaltInfoZulage> gehaltZulagen = gehaltInfoZulageService.findAllByGehaltInfoId(gehaltInfo.getId());
        for (GehaltInfoZulage gehaltInfoZulage : gehaltZulagen) {
            zulagenTotal = zulagenTotal.add(gehaltInfoZulage.getZulageInEuro());
        }

        int maxMonths = calculateMaxMonths(vertragsdaten.getEintritt());

        KVStufe stufe;
        // Ensure the KV-Stufe is within the allowed range
        if (totalMonths > maxMonths) {
            totalMonths = maxMonths;
        }
        stufe = kvStufeService.findByTotalMonths(totalMonths);

        ArbeitszeitenInfo arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(getVertragsdaten().getId());
        if (arbeitszeitenInfo != null) {
            Gehalt gehalt = gehaltService.findCurrentByKvStufeAndVerwendungsgruppeAndStatus(stufe, gehaltInfo.getVerwendungsgruppe(), Status.ACTIVE, vertragsdaten.getEintritt());
            if (gehalt != null && gehalt.getGehalt() != null && !isNullOrBlank(arbeitszeitenInfo.getWochenstunden())) {
                gehaltInfo.setKvGehaltBerechnet(gehalt.getGehalt().multiply(parseStringToBigDecimal(arbeitszeitenInfo.getWochenstunden())).setScale(2, RoundingMode.HALF_UP));
                if (gehaltInfo.getGehaltVereinbart() != null && gehaltInfo.getGehaltVereinbart().compareTo(gehaltInfo.getKvGehaltBerechnet()) > 0) {
                    gehaltInfo.setUeberzahlung(gehaltInfo.getGehaltVereinbart().subtract(gehaltInfo.getKvGehaltBerechnet()).setScale(2, RoundingMode.HALF_UP));
                } else {
                    gehaltInfo.setGehaltVereinbart(gehaltInfo.getKvGehaltBerechnet());
                    gehaltInfo.setUeberzahlung(BigDecimal.valueOf(0));
                }
                gehaltInfo.setGesamtBrutto(gehaltInfo.getGehaltVereinbart().add(zulagenTotal));
                if (gehaltInfo.getUestPauschale() != null && !gehaltInfo.getUestPauschale().equals(BigDecimal.valueOf(0))) {
                    gehaltInfo.setGesamtBrutto(gehaltInfo.getGesamtBrutto().add(gehaltInfo.getUestPauschale()));
                }
            }
        } else {
            return false;
        }


        gehaltInfo.setAngerechneteFacheinschlaegigeTaetigkeitenMonate(totalMonths);

        gehaltInfo.setAngerechneteFreieTaetigkeitenMonate(getFreeMonths().setScale(0, RoundingMode.HALF_UP).intValue());
        gehaltInfo.setStufe(stufe);
        gehaltInfo.setNaechsteStufeDatum(nextStufeDate);
        gehaltInfoService.save(gehaltInfo);
        return true;
    }

    @Override
    public int calculateTotalMonths(List<Vordienstzeiten> vordienstzeitens) {
        setFreeVertragsarten();
        List<Vordienstzeiten> mutableVordienstzeitens = new ArrayList<>(vordienstzeitens);
        mutableVordienstzeitens.sort(Comparator.comparing(Vordienstzeiten::getVon));

        double totalMonths;
        double fixMonths = 0;
        double freeMonths = 0;

        // Separate fixed and free periods for individual processing
        List<Vordienstzeiten> fixedPeriods = new ArrayList<>();
        List<Vordienstzeiten> freePeriods = new ArrayList<>();

        for (Vordienstzeiten v : mutableVordienstzeitens) {
            if (v.getVertragsart().getName().equals(FIX_VERTRAGSART)) {
                fixedPeriods.add(v);
            } else if (getFreeVertragsarten().contains(v.getVertragsart().getName())) {
                freePeriods.add(v);
            }
        }

        // Combine overlapping fixed periods
        List<Vordienstzeiten> combinedFixedPeriods = combineOverlappingPeriods(fixedPeriods);

        // Calculate the fixed contract months
        for (Vordienstzeiten v : combinedFixedPeriods) {
            fixMonths += calculateFixedMonths(v);
        }

        // Combine overlapping free periods
        List<Vordienstzeiten> combinedFreePeriods = combineOverlappingFreePeriods(freePeriods);

        // Calculate the free contract months, considering overlaps with fixed contracts
        for (Vordienstzeiten v : combinedFreePeriods) {
            freeMonths += calculateFreeMonths(v, combinedFixedPeriods);
        }

        setFreeMonths(BigDecimal.valueOf(freeMonths));
        setFixMonths(BigDecimal.valueOf(fixMonths));

        totalMonths = getFixMonths()
                .add(getFreeMonths()) // Add the two BigDecimal values
                .setScale(0, RoundingMode.HALF_UP) // Round to the nearest integer
                .intValue(); // Convert to int
        return (int) Math.round(totalMonths);  // Round the result to the nearest whole number
    }

    private List<Vordienstzeiten> combineOverlappingPeriods(List<Vordienstzeiten> periods) {
        List<Vordienstzeiten> combined = new ArrayList<>();
        LocalDate currentStart = null;
        LocalDate currentEnd = null;
        Vordienstzeiten currentVordienstzeiten = null;

        for (Vordienstzeiten v : periods) {
            LocalDate start = v.getVon();
            LocalDate end = v.getBis();
            String vertragsartName = v.getVertragsart().getName();

            if (currentStart == null) {
                currentStart = start;
                currentEnd = end;
                currentVordienstzeiten = v;
            } else if (vertragsartName.equals(currentVordienstzeiten.getVertragsart().getName()) && !start.isAfter(currentEnd)) {
                // Combine overlapping periods of the same contract type
                if (end.isAfter(currentEnd)) {
                    currentEnd = end;
                }
            } else {
                combined.add(new Vordienstzeiten(
                        currentVordienstzeiten.getVertragsdaten(),
                        currentVordienstzeiten.getVertragsart(),
                        currentVordienstzeiten.getFirma(),
                        currentStart,
                        currentEnd,
                        currentVordienstzeiten.getWochenstunden(),
                        currentVordienstzeiten.isAnrechenbar(),
                        currentVordienstzeiten.getNachweisStatus(),
                        currentVordienstzeiten.getStatus(),
                        currentVordienstzeiten.getCreatedBy()
                ));
                currentStart = start;
                currentEnd = end;
                currentVordienstzeiten = v;
            }
        }

        if (currentStart != null) {
            combined.add(new Vordienstzeiten(
                    currentVordienstzeiten.getVertragsdaten(),
                    currentVordienstzeiten.getVertragsart(),
                    currentVordienstzeiten.getFirma(),
                    currentStart,
                    currentEnd,
                    currentVordienstzeiten.getWochenstunden(),
                    currentVordienstzeiten.isAnrechenbar(),
                    currentVordienstzeiten.getNachweisStatus(),
                    currentVordienstzeiten.getStatus(),
                    currentVordienstzeiten.getCreatedBy()
            ));
        }

        return combined;
    }

    // Method to combine overlapping free periods
    private List<Vordienstzeiten> combineOverlappingFreePeriods(List<Vordienstzeiten> periods) {
        List<Vordienstzeiten> combined = new ArrayList<>();
        periods.sort(Comparator.comparing(Vordienstzeiten::getVon)); // Ensure periods are sorted by start date

        LocalDate currentStart = null;
        LocalDate currentEnd = null;
        double currentWochenstunden = 0;

        for (Vordienstzeiten v : periods) {
            LocalDate start = v.getVon();
            LocalDate end = v.getBis();
            double wochenstunden = v.getWochenstunden();

            if (currentStart == null) {
                currentStart = start;
                currentEnd = end;
                currentWochenstunden = wochenstunden;
            } else if (!start.isAfter(currentEnd)) {
                // Periods overlap
                if (end.isAfter(currentEnd)) {
                    //currentEnd = end;
                    currentWochenstunden = Math.min(38, currentWochenstunden + wochenstunden);
                    combined.add(new Vordienstzeiten(
                            periods.get(0).getVertragsdaten(),
                            periods.get(0).getVertragsart(),
                            periods.get(0).getFirma(),
                            currentStart,
                            currentEnd,
                            currentWochenstunden,
                            periods.get(0).isAnrechenbar(),
                            periods.get(0).getNachweisStatus(),
                            periods.get(0).getStatus(),
                            periods.get(0).getCreatedBy()
                    ));
                    currentStart = currentEnd.plusDays(1);
                    currentEnd = end;
                    currentWochenstunden = wochenstunden;
                }
            }
        }

        if (currentStart != null) {
            combined.add(new Vordienstzeiten(
                    periods.get(0).getVertragsdaten(),
                    periods.get(0).getVertragsart(),
                    periods.get(0).getFirma(),
                    currentStart,
                    currentEnd,
                    currentWochenstunden,
                    periods.get(0).isAnrechenbar(),
                    periods.get(0).getNachweisStatus(),
                    periods.get(0).getStatus(),
                    periods.get(0).getCreatedBy()
            ));
        }

        return combined;
    }

    private double calculateFixedMonths(Vordienstzeiten v) {
        LocalDate start = v.getVon();
        LocalDate end = v.getBis();
        long days = ChronoUnit.DAYS.between(start, end.plusDays(1));
        return (days / DAYS_IN_YEAR) * (double) MONTHS_IN_YEAR;
    }

    private double calculateFreeMonths(Vordienstzeiten freeV, List<Vordienstzeiten> allPeriods) {
        LocalDate start = freeV.getVon();
        LocalDate end = freeV.getBis();
        double wochenstunden = freeV.getWochenstunden();
        long totalDays = ChronoUnit.DAYS.between(start, end.plusDays(1));
        double totalMonths = 0;

        // Adjust for overlaps with fixed contracts
        for (Vordienstzeiten v : allPeriods) {
            if (v.getVertragsart().getName().equals(FIX_VERTRAGSART)) {
                LocalDate fixedStart = v.getVon();
                LocalDate fixedEnd = v.getBis();
                if (start.isBefore(fixedEnd) && end.isAfter(fixedStart)) {
                    if (start.isBefore(fixedStart) && end.isAfter(fixedEnd)) {
                        // Split the free period into two non-overlapping parts
                        long part1Days = ChronoUnit.DAYS.between(start, fixedStart.minusDays(1));
                        long part2Days = ChronoUnit.DAYS.between(fixedEnd.plusDays(1), end.plusDays(1));
                        totalDays = part1Days + part2Days;
                    } else if (start.isBefore(fixedStart)) {
                        totalDays = ChronoUnit.DAYS.between(start, fixedStart.minusDays(1));
                    } else if (end.isAfter(fixedEnd)) {
                        totalDays = ChronoUnit.DAYS.between(fixedEnd.plusDays(1), end.plusDays(1));
                    } else {
                        return 0; // Completely overlaps with a fixed contract
                    }
                }
            }
        }

        double months = (totalDays / DAYS_IN_YEAR) * (double) MONTHS_IN_YEAR;
        totalMonths += months * (wochenstunden / fullTimeHours);

        // Combine overlapping free contracts up to a maximum of 38 hours per week
        for (Vordienstzeiten v : allPeriods) {
            if (!v.getVertragsart().getName().equals(FIX_VERTRAGSART) && v != freeV) {
                LocalDate otherStart = v.getVon();
                LocalDate otherEnd = v.getBis();
                double otherWochenstunden = v.getWochenstunden();

                if (start.isBefore(otherEnd) && end.isAfter(otherStart)) {
                    // Adjust the overlapping period
                    LocalDate overlapStart = start.isAfter(otherStart) ? start : otherStart;
                    LocalDate overlapEnd = end.isBefore(otherEnd) ? end : otherEnd;
                    long overlapDays = ChronoUnit.DAYS.between(overlapStart, overlapEnd.plusDays(1));

                    // Calculate the combined hours for the overlapping period
                    double combinedWochenstunden = Math.min(38, wochenstunden + otherWochenstunden);
                    double overlapMonths = (overlapDays / DAYS_IN_YEAR) * (double) MONTHS_IN_YEAR * (combinedWochenstunden / fullTimeHours);

                    // Subtract the overlap period from the total days and add the combined months
                    totalDays -= overlapDays;
                    double remainingMonths = (totalDays / DAYS_IN_YEAR) * (double) MONTHS_IN_YEAR * (wochenstunden / fullTimeHours);

                    return totalMonths + remainingMonths + overlapMonths;
                }
            }
        }

        return totalMonths;
    }

    public LocalDate calculateNextStufeDate(LocalDate entryDate, int totalMonths) {
        List<KVStufe> kvStufen = kvStufeService.findAll();

        KVStufe currentStufe = null;
        KVStufe nextStufe = null;
        int monthsInCurrentStufe = 0;

        for (int i = 0; i < kvStufen.size(); i++) {
            KVStufe stufe = kvStufen.get(i);
            if (totalMonths >= stufe.getMinMonths() && (stufe.getMaxMonths() == null || totalMonths < stufe.getMaxMonths())) {
                currentStufe = stufe;
                if (i < kvStufen.size() - 1) {
                    nextStufe = kvStufen.get(i + 1);
                    monthsInCurrentStufe = stufe.getMaxMonths() - totalMonths;
                }
                break;
            }
        }

        if (currentStufe == null || nextStufe == null) {
            log.error("Cannot determine current or next Stufe.");
        }

        LocalDate nextStufeDate = entryDate.plusMonths(monthsInCurrentStufe);
        if (entryDate.getDayOfMonth() != 1) {
            nextStufeDate = nextStufeDate.withDayOfMonth(1);
        }

        return nextStufeDate;
    }


    private void setVordienstzeiten(String personalNummer) {
        if (getVertragsdaten() != null) {
            List<Vordienstzeiten> validatedVordienstzeiten = new ArrayList<>(vordienstzeitenService.findAllByVertragsdatenId(getVertragsdaten().getId()).stream().filter(vdz -> vdz.getStatus().equals(MitarbeiterStatus.VALIDATED) && vdz.isAnrechenbar()).toList());
            validatedVordienstzeiten.sort(Comparator.comparing(Vordienstzeiten::getVon));
            setVordienstzeitens(validatedVordienstzeiten);
        } else {
            log.warn("No vertragsdaten found for personalnummer: {}", personalNummer);
        }
    }

    private void setFreeVertragsarten() {
        List<String> freeVertragsarten = new ArrayList<>(vertragsartService.findAll().stream().map(Vertragsart::getName).toList());
        freeVertragsarten.remove(FIX_VERTRAGSART);
        setFreeVertragsarten(freeVertragsarten);
    }

    private void startWorkflowitem(String personalNummer) {
        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(personalNummer, SWorkflows.ERFASSEN_STAMMDATEN_VERTRAGSDATEN);
        if (workflow != null) {
            setWorkflow(workflow);
            manageWFItemsService.setWFItemStatus(getWorkflow(), SWorkflowItems.KV_EINSTUFUNG_BERECHNEN, WWorkflowStatus.IN_PROGRESS, globalUserHolder.getUsername());
        } else {
            log.warn("No workflow found for personalnummer: {}", personalNummer);
        }
    }
}

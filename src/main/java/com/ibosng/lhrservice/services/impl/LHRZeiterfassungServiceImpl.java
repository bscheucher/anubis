package com.ibosng.lhrservice.services.impl;

import com.ibosng._service.AsyncService;
import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.zeiterfassung.ZeiterfassungTransferDto;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.workflows.WWorkflow;
import com.ibosng.dbservice.entities.workflows.WWorkflowItem;
import com.ibosng.dbservice.entities.workflows.WWorkflowStatus;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungTransfer;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import com.ibosng.dbservice.services.workflows.WWorkflowItemService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungService;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungTransferService;
import com.ibosng.lhrservice.client.LHRClient;
import com.ibosng.lhrservice.dtos.DnEintritteDto;
import com.ibosng.lhrservice.dtos.variabledaten.EintrittDto;
import com.ibosng.lhrservice.dtos.variabledaten.ZeitangabeDto;
import com.ibosng.lhrservice.exceptions.LHRException;
import com.ibosng.lhrservice.services.HelperService;
import com.ibosng.lhrservice.services.LHREnvironmentService;
import com.ibosng.lhrservice.services.LHRUrlaubService;
import com.ibosng.lhrservice.services.LHRZeiterfassungService;
import com.ibosng.workflowservice.enums.SWorkflowItems;
import com.ibosng.workflowservice.enums.SWorkflows;
import com.ibosng.workflowservice.services.ManageWFItemsService;
import com.ibosng.workflowservice.services.ManageWFsService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.lhrservice.utils.Constants.*;
import static com.ibosng.lhrservice.utils.Helpers.getDateInPostRequestFormat;
import static com.ibosng.lhrservice.utils.Parsers.mergeLocalDateTime;
import static com.ibosng.lhrservice.utils.Parsers.parseStringToInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class LHRZeiterfassungServiceImpl implements LHRZeiterfassungService {
    private final ManageWFsService manageWFsService;
    private final ManageWFItemsService manageWFItemsService;
    private final ZeiterfassungTransferService zeiterfassungTransferService;
    private final ZeiterfassungService zeiterfassungService;
    private final LHRClient lhrClient;
    private final LHREnvironmentService lhrEnvironmentService;
    private final PersonalnummerService personalnummerService;
    private final ZeitausgleichService zeitausgleichService;
    private final LHRUrlaubService lhrUrlaubService;
    private final WWorkflowItemService wWorkflowItemService;
    private final HelperService helperService;
    private final ArbeitszeitenService arbeitszeitenService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;
    private final VertragsdatenService vertragsdatenService;
    private final AsyncService asyncService;

    @Getter
    @Setter
    private Set<String> notRegisteredPersonalnummerInLHR = new HashSet<>();

    @Getter
    @Setter
    private Set<String> notRegisteredSVNRInLHR = new HashSet<>();


    @Override
    public ResponseEntity<ZeiterfassungTransferDto> sendZeiterfassungTransfer(String zeiterfassungTransferId, String createdBy) {
        setNotRegisteredPersonalnummerInLHR(new HashSet<>());
        setNotRegisteredSVNRInLHR(new HashSet<>());
        Optional<ZeiterfassungTransfer> zeiterfassungTransferOptional = zeiterfassungTransferService.findById(parseStringToInteger(zeiterfassungTransferId));
        if (zeiterfassungTransferOptional.isEmpty()) {
            log.error("Could not find zeiterfassungTransfer object for id: {}", zeiterfassungTransferId);
            return ResponseEntity.notFound().build();
        }
        ZeiterfassungTransfer zeiterfassungTransfer = zeiterfassungTransferOptional.get();

        WWorkflow workflow = manageWFsService.getWorkflowFromDataAndWFType(zeiterfassungTransferId, SWorkflows.TN_AN_ABWESENHEITEN_TRANSFER_LHR);

        if (workflow == null) {
            log.error("Could not find workflow for id: {}", zeiterfassungTransferId);
            zeiterfassungTransfer.setStatus(ZeiterfassungStatus.ERROR);
            zeiterfassungTransferService.save(zeiterfassungTransfer);
            return ResponseEntity.notFound().build();
        }

        manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_AN_ABWESENHEITEN_LHR_UEBERTRAGEN, WWorkflowStatus.IN_PROGRESS, createdBy);

        List<Zeiterfassung> zeiterfassungen = zeiterfassungService.findAllByZeiterfassungTransferIdAndStatus(zeiterfassungTransfer.getId(), ZeiterfassungStatus.VALID);

        Map<Personalnummer, List<Zeiterfassung>> teilnehmer2Zeiterfassung = zeiterfassungen.stream().filter(ze -> ze.getTeilnehmer().getPersonalnummer() != null)
                .collect(Collectors.groupingBy(ze -> ze.getTeilnehmer().getPersonalnummer()));

        for (Personalnummer personalnummer : teilnehmer2Zeiterfassung.keySet()) {
            if (!isNullOrBlank(personalnummer.getPersonalnummer())) {
                try {
                    DnEintritteDto dnEintritte = new DnEintritteDto();
                    dnEintritte.setDienstnehmer(helperService.createDienstnehmerRefDto(personalnummer));
                    dnEintritte.setEintritte(groupConsecutiveZeiterfassungen(teilnehmer2Zeiterfassung.get(personalnummer)));
                    log.info("Sending zeiterfassungen to LHR.");
                    lhrClient.postEintritt(lhrEnvironmentService.getFaKz(personalnummer.getFirma()),
                            lhrEnvironmentService.getFaNr(personalnummer.getFirma()), parseStringToInteger(personalnummer.getPersonalnummer()), null, null, dnEintritte);
                } catch (LHRException e) {
                    log.error(e.getMessage());
                }
            }
        }

        if (getNotRegisteredPersonalnummerInLHR().isEmpty()) { // TODO: ALWAYS TRUE, remove
            manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_AN_ABWESENHEITEN_LHR_UEBERTRAGEN, WWorkflowStatus.COMPLETED, createdBy);
            zeiterfassungTransfer.setStatus(ZeiterfassungStatus.COMPLETED);
        } else { // NEVER GETS EXECUTED
            WWorkflowItem wWorkflowItem = manageWFItemsService.setWFItemStatus(workflow, SWorkflowItems.TN_AN_ABWESENHEITEN_LHR_UEBERTRAGEN, WWorkflowStatus.ERROR, createdBy);
            wWorkflowItem.setData(
                    String.format("The TN is not registered in LHR with personalnummer %s and SVNR %s",
                            String.join(",", getNotRegisteredPersonalnummerInLHR()),
                            getNotRegisteredSVNRInLHR().stream()
                                    .map(String::valueOf)
                                    .collect(Collectors.joining(","))));
            wWorkflowItemService.save(wWorkflowItem);
            if (getNotRegisteredPersonalnummerInLHR().size() < teilnehmer2Zeiterfassung.keySet().size()) {
                zeiterfassungTransfer.setStatus(ZeiterfassungStatus.PARTIALLY_COMPLETED);
            } else {
                zeiterfassungTransfer.setStatus(ZeiterfassungStatus.ERROR);
            }

        }

        zeiterfassungTransfer.setDatumSent(getLocalDateNow());
        zeiterfassungTransfer = zeiterfassungTransferService.save(zeiterfassungTransfer);
        log.info("Zeiterfassungstransfer saved");
        return ResponseEntity.status(HttpStatus.OK).body(zeiterfassungTransferService.mapZeiterfassungTransferToDto(zeiterfassungTransfer));
    }

    private EintrittDto mapZeiterfassung2DnEintritt(ZeiterfassungReason zeiterfassungReason, String von, String bis) {
        EintrittDto eintritt = new EintrittDto();
        eintritt.setGrund(zeiterfassungReason.getLhrKz());
        eintritt.setBeschreibung(zeiterfassungReason.getBezeichnung());
        eintritt.setKommentar(zeiterfassungReason.getBezeichnung());
        eintritt.setSource(TERMINAL);
        eintritt.setZeitangabe(new ZeitangabeDto(von, bis));

        return eintritt;
    }

    public List<EintrittDto> groupConsecutiveZeiterfassungen(List<Zeiterfassung> zeiterfassungen) {
        // Sort by date first
        List<Zeiterfassung> sortedList = zeiterfassungen.stream()
                .sorted(Comparator.comparing(Zeiterfassung::getDatum))
                .toList();

        List<List<Zeiterfassung>> groupedResults = new ArrayList<>();
        List<Zeiterfassung> currentGroup = new ArrayList<>();
        List<EintrittDto> eintritts = new ArrayList<>();

        for (int i = 0; i < sortedList.size(); i++) {
            Zeiterfassung current = sortedList.get(i);

            if (currentGroup.isEmpty()) {
                currentGroup.add(current);
            } else {
                Zeiterfassung lastInGroup = currentGroup.get(currentGroup.size() - 1);

                // Check if date is consecutive and reason is the same
                if (ChronoUnit.DAYS.between(lastInGroup.getDatum(), current.getDatum()) == 1 &&
                        lastInGroup.getZeiterfassungReason().equals(current.getZeiterfassungReason())) {
                    currentGroup.add(current);
                } else {
                    LocalDate firstDate = currentGroup.get(0).getDatum();
                    LocalDate lastDate = currentGroup.get(currentGroup.size() - 1).getDatum();
                    eintritts.add(mapZeiterfassung2DnEintritt(currentGroup.get(0).getZeiterfassungReason(), getDateInPostRequestFormat(firstDate), getDateInPostRequestFormat(lastDate)));
                    //groupedResults.add(new ArrayList<>(currentGroup)); // Store the current group
                    currentGroup.clear();
                    currentGroup.add(current); // Start a new group
                }
            }
        }

        // Add the last group if not empty
        if (!currentGroup.isEmpty()) {
            LocalDate firstDate = currentGroup.get(0).getDatum();
            LocalDate lastDate = currentGroup.get(currentGroup.size() - 1).getDatum();
            eintritts.add(mapZeiterfassung2DnEintritt(currentGroup.get(0).getZeiterfassungReason(), getDateInPostRequestFormat(firstDate), getDateInPostRequestFormat(lastDate)));
        }

        return eintritts;
    }

    @Override
    public ResponseEntity<List<ZeitausgleichDto>> submitZeitausgleichForPeriod(AbwesenheitDto abwewsenheitenDto) {
        Personalnummer pn = personalnummerService.findById(abwewsenheitenDto.getPersonalnummerId()).orElse(null);
        if (pn == null || pn.getFirma() == null) {
            log.error("Personalnummer object not found for ID {} in submitZeitausgleichForPeriod", abwewsenheitenDto.getPersonalnummerId());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (!zeitausgleichService.findByPersonalnummerInPeriod(
                abwewsenheitenDto.getPersonalnummerId(), abwewsenheitenDto.getStartDate(), abwewsenheitenDto.getEndDate(),
                List.of(AbwesenheitStatus.VALID, AbwesenheitStatus.ACCEPTED)).isEmpty()) {

            log.info("Valid/Accepted zeitausgleich already exisits in period {}-{} for personalnummer {}",
                    abwewsenheitenDto.getStartDate(), abwewsenheitenDto.getEndDate(), abwewsenheitenDto.getPersonalnummerId());

            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        final String firma = lhrEnvironmentService.getFaKz(pn.getFirma());
        final Integer firmaNr = lhrEnvironmentService.getFaNr(pn.getFirma());

        boolean result = true;
        for (LocalDate i = abwewsenheitenDto.getStartDate(); !i.isAfter(abwewsenheitenDto.getEndDate()); i = i.plusDays(1)) {
            Double nettoHours = findNetto(pn, i);
            log.info("date {}, netto {}", i, nettoHours);
            if (nettoHours == null || nettoHours <= 0) {
                continue;
            }

            final String dateTimeVon = mergeLocalDateTime(i, TIME_VON);
            final String dateTimeBis = mergeLocalDateTime(i, LocalTime.of(nettoHours.intValue(), 0));

            result &= lhrUrlaubService.sendZeitausgleichToLhr(pn.getId(), firmaNr, firma, BUCHUNGANFRAGE_I, dateTimeVon, dateTimeBis);
        }

        final List<ZeitausgleichDto> zeitausgleichDtos = createAndSaveZeitausgleiche(pn, abwewsenheitenDto.getStartDate(), abwewsenheitenDto.getEndDate(),
                abwewsenheitenDto.getComment(), result ? AbwesenheitStatus.VALID : AbwesenheitStatus.ERROR)
                .stream().map(zeitausgleichService::map).toList();

        asyncService.asyncExecutor(() -> lhrUrlaubService.syncUrlaubDetails(pn.getId(), abwewsenheitenDto.getStartDate().toString(), abwewsenheitenDto.getEndDate().toString()));

        return ResponseEntity.ok().body(zeitausgleichDtos);
    }

    /**
     * Check existence of zeitausgleich before usage! For buchungType=D it must exist, for I - it mustn`t exist
     */


    private List<Zeitausgleich> createAndSaveZeitausgleiche(Personalnummer personalnummer, LocalDate startDate,
                                                            LocalDate endDate, String comment, AbwesenheitStatus status) {
        List<Zeitausgleich> zeitausgleiche = new ArrayList<>();

        for (LocalDate i = startDate; !i.isAfter(endDate); i = i.plusDays(1)) {
            Double netto = findNetto(personalnummer, i);
            if (netto == null) {
                continue;
            }

            Zeitausgleich zeitausgleich = Zeitausgleich.builder()
                    .personalnummer(personalnummer)
                    .datum(i)
                    .timeVon(LocalTime.of(0, 0))
                    .timeBis(LocalTime.of(netto.intValue(), 0))
                    .status(status)
                    .comment(comment)
                    .createdBy(LHR_SERVICE)
                    .build();

            zeitausgleiche.add(zeitausgleichService.save(zeitausgleich));
        }
        return zeitausgleiche;
    }

    private Double findNetto(Personalnummer personalnummer, LocalDate date) {
        Arbeitszeiten arbeitszeiten = vertragsdatenService
                .findAllByPNAndStatusesNotInVertragsdatenaenderungen(personalnummer, List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED))
                .stream()
                .findFirst()
                .map(vd -> arbeitszeitenInfoService.findByVertragsdatenId(vd.getId()))
                .filter(ai -> List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED).contains(ai.getStatus()))
                .stream()
                .flatMap(a -> arbeitszeitenService.findByArbAndArbeitszeitenInfoId(a.getId()).stream())
                .findFirst()
                .orElse(null);

        if (arbeitszeiten == null) {
            log.warn("No active arbeitszeiten for personalnummer: {}, bmdClient: {}", personalnummer.getPersonalnummer(),
                    personalnummer.getFirma().getBmdClient());
            return null;
        }

        return switch (date.getDayOfWeek()) {
            case MONDAY -> arbeitszeiten.getMontagNetto();
            case TUESDAY -> arbeitszeiten.getDienstagNetto();
            case WEDNESDAY -> arbeitszeiten.getMittwochNetto();
            case THURSDAY -> arbeitszeiten.getDonnerstagNetto();
            case FRIDAY -> arbeitszeiten.getFreitagNetto();
            case SATURDAY -> arbeitszeiten.getSamstagNetto();
            case SUNDAY -> arbeitszeiten.getSonntagNetto();
        };
    }
}

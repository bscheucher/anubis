package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.*;
import com.ibosng.dbservice.dtos.teilnehmer.*;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungCsvDto;
import com.ibosng.dbservice.dtos.zeiterfassung.export.PruefungXlsxDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Land;
import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import com.ibosng.dbservice.entities.mitarbeiter.datastatus.StammdatenDataStatus;
import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.seminar.SeminarPruefung;
import com.ibosng.dbservice.entities.seminar.SeminarPruefungNiveau;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerDataStatus;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungReason;
import com.ibosng.dbservice.repositories.TeilnehmerRepository;
import com.ibosng.dbservice.repositories.TeilnehmerSpecification;
import com.ibosng.dbservice.services.SeminarPruefungService;
import com.ibosng.dbservice.services.TeilnehmerService;
import com.ibosng.dbservice.services.mitarbeiter.ArbeitszeitenInfoService;
import com.ibosng.dbservice.services.mitarbeiter.GehaltInfoService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.mitarbeiter.VertragsdatenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Mappers.mapTeilnehmerSeminarDtoToCsvDto;
import static com.ibosng.dbservice.utils.Mappers.mapTeilnehmerSeminarDtoToXlsxDto;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseTimestampSqlToLocalDate;

@Service
public class TeilnehmerServiceImpl implements TeilnehmerService {
    private final TeilnehmerRepository teilnehmerRepository;
    private final TelefonServiceImpl telefonService;
    private final SeminarPruefungService seminarPruefungService;
    private final StammdatenService stammdatenService;
    private final VertragsdatenService vertragsdatenService;
    private final GehaltInfoService gehaltInfoService;
    private final ArbeitszeitenInfoService arbeitszeitenInfoService;

    @Autowired
    public TeilnehmerServiceImpl(TeilnehmerRepository teilnehmerRepository,
                                 TelefonServiceImpl telefonService,
                                 SeminarPruefungService seminarPruefungService,
                                 StammdatenService stammdatenService,
                                 VertragsdatenService vertragsdatenService,
                                 GehaltInfoService gehaltInfoService,
                                 ArbeitszeitenInfoService arbeitszeitenInfoService) {
        this.teilnehmerRepository = teilnehmerRepository;
        this.telefonService = telefonService;
        this.seminarPruefungService = seminarPruefungService;
        this.stammdatenService = stammdatenService;
        this.vertragsdatenService = vertragsdatenService;
        this.gehaltInfoService = gehaltInfoService;
        this.arbeitszeitenInfoService = arbeitszeitenInfoService;
    }

    @Override
    public List<PruefungCsvDto> findTeilnehmerForPruefungCsv(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, Sort.Direction direction, int page, int size) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        Page<Teilnehmer> teilnehmers = teilnehmerRepository.findTeilnehmerFiltered(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, null, null, null, sortProperty, direction, page, size);
        List<TeilnehmerSeminarDto> teilnehmerList = new ArrayList<>();
        for (Teilnehmer tn : teilnehmers.getContent()) {

            teilnehmerList.add(findTeilnehmerDtoById(tn.getId()));
        }

        List<PruefungCsvDto> pruefungCsvList = new ArrayList<>();
        for (TeilnehmerSeminarDto dto : teilnehmerList) {
            PruefungCsvDto csvDto = mapTeilnehmerSeminarDtoToCsvDto(dto);
            if (csvDto != null) {
                pruefungCsvList.add(csvDto);
            }
        }

        return pruefungCsvList;
    }

    @Override
    public List<PruefungXlsxDto> findTeilnehmerForPruefungXlsx(String identifiersString, String seminarName, String projektName, boolean isActive, Boolean isUebaTeilnehmer, boolean isAngemeldet, String geschlecht, String sortProperty, Sort.Direction direction, int page, int size) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        Page<Teilnehmer> teilnehmers = teilnehmerRepository.findTeilnehmerFiltered(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, null, null, null, sortProperty, direction, page, size);
        List<TeilnehmerSeminarDto> teilnehmerList = new ArrayList<>();
        for (Teilnehmer tn : teilnehmers.getContent()) {

            teilnehmerList.add(findTeilnehmerDtoById(tn.getId()));
        }

        List<PruefungXlsxDto> pruefungXlsxList = new ArrayList<>();
        for (TeilnehmerSeminarDto dto : teilnehmerList) {
            PruefungXlsxDto csvDto = mapTeilnehmerSeminarDtoToXlsxDto(dto);
            if (csvDto != null) {
                pruefungXlsxList.add(csvDto);
            }
        }

        return pruefungXlsxList;
    }

    @Override
    public Optional<Teilnehmer> findById(Integer id) {
        return teilnehmerRepository.findById(id);
    }

    @Override
    public List<Teilnehmer> findAll() {
        return teilnehmerRepository.findAll();
    }

    @Override
    public Teilnehmer save(Teilnehmer teilnehmer) {
        telefonService.saveAll(teilnehmer.getTelefons());
        return teilnehmerRepository.save(teilnehmer);
    }

    @Override
    public List<Teilnehmer> saveAll(List<Teilnehmer> teilnehmers) {
        return teilnehmerRepository.saveAll(teilnehmers);
    }

    @Override
    public void deleteById(Integer id) {
        this.teilnehmerRepository.deleteById(id);
    }

    @Override
    public List<Teilnehmer> findAllByIdentifier(String importFilename) {
        return teilnehmerRepository.findAllByImportFilename(importFilename);
    }

    @Override
    public List<Teilnehmer> getBySVN(String svn) {
        return teilnehmerRepository.findBySvNummer(svn);
    }

    @Override
    public List<Teilnehmer> getByVorname(String vorname) {
        return teilnehmerRepository.findByVorname(vorname);
    }

    @Override
    public List<Teilnehmer> getByNachname(String nachname) {
        return teilnehmerRepository.findByNachname(nachname);
    }

    @Override
    public List<Teilnehmer> getByVornameAndNachname(String vorname, String nachname) {
        return teilnehmerRepository.findByVornameAndNachname(vorname, nachname);
    }

    @Override
    public List<NewParticipantsSummaryDto> getSummaryImportedTeilnehmer(LocalDate date) {
        List<Object[]> rawData = teilnehmerRepository.getSummaryImportedTeilnehmer(date);
        return rawData.stream()
                .map(this::mapNewParticipantsSummaryDto)
                .toList();
    }

    @Override
    public Page<Teilnehmer> findAllErroneousParticipants(Pageable pageable, String sortBy, Sort.Direction direction) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return teilnehmerRepository.findByStatus(TeilnehmerStatus.INVALID, sortedPageable);
    }

    @Override
    public List<Teilnehmer> findAllCreatedOrChangedAfter(LocalDateTime after) {
        return teilnehmerRepository.findAllByCreatedOnAfterOrChangedOnAfter(after, after);
    }

    @Override
    public List<String> findSeminarsForParticipantsWithStatus(TeilnehmerStatus status) {
        return teilnehmerRepository.getSeminarsForTeilnehmerWithStatus(status);
    }

    @Override
    public List<String> findMassnahmenummersForParticipantsWithStatus(TeilnehmerStatus status) {
        return teilnehmerRepository.getMassnahmenummersForTeilnehmerWithStatus(status);
    }

    @Override
    public List<Teilnehmer> findTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar, int page, int size) {
        int offset = page * size;
        return teilnehmerRepository.getTeilnehmerWithStatusAndSeminar(status, seminar, size, offset);
    }

    @Override
    public List<Teilnehmer> findTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer, int page, int size) {
        int offset = page * size;
        return teilnehmerRepository.getTeilnehmerWithStatusAndMassnahmenummer(status, massnahmenummer, size, offset);
    }

    @Override
    public Integer getCountTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer) {
        return teilnehmerRepository.getCountTeilnehmerWithStatusAndMassnahmenummer(status, massnahmenummer);
    }

    @Override
    public Integer getCountTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar) {
        return teilnehmerRepository.getCountTeilnehmerWithStatusAndSeminar(status, seminar);
    }

    @Override
    public Page<TeilnehmerFilterSummaryDto> findTeilnehmerFiltered(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, String sortProperty, Sort.Direction direction, int page, int size) {
        if (isNullOrBlank(sortProperty)) {
            sortProperty = "nachname";
        }
        int offset = page * size;


        long count = teilnehmerRepository.findTeilnehmerFilteredCount(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId);

        List<Object[]> rawData = switch (sortProperty + direction.name()) {
            case "vornameASC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeVornameAsc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            case "vornameDESC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeVornameDesc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            case "nachnameASC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeNachnameAsc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            case "nachnameDESC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeNachnameDesc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            case "svnASC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeSvnrAsc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            case "svnDESC" ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeSvnrDesc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
            default ->
                    teilnehmerRepository.findTeilnehmerFilteredNativeNachnameAsc(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzerId, size, offset);
        };
        return mapTeilnehmerToFilteredSummary(rawData, count, sortProperty, direction, page, size);
    }

    @Override
    public Page<Teilnehmer> findUebaTeilnehmerByCriteria(String searchTerm, String sortBy, Sort.Direction direction, int page, int size) {
        Specification<Teilnehmer> spec = TeilnehmerSpecification.filterTeilnehmer(searchTerm);
        if (isNullOrBlank(sortBy)) {
            sortBy = "nachname";
        }
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return teilnehmerRepository.findAll(spec, pageable);
    }

    @Override
    public TeilnehmerSeminarDto findTeilnehmerDtoById(Integer id) {
        Optional<Teilnehmer> optionalTeilnehmer = findById(id);

        if (optionalTeilnehmer.isPresent()) {
            Teilnehmer teilnehmer = optionalTeilnehmer.get();

            Stammdaten stammdaten = null;
            Vertragsdaten vertragsdaten = null;
            GehaltInfo gehaltInfo = null;
            ArbeitszeitenInfo arbeitszeitenInfo = null;
            if (teilnehmer.getPersonalnummer() != null) {
                stammdaten = stammdatenService.findByPersonalnummerAndStatusIn(teilnehmer.getPersonalnummer(), List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED));
                vertragsdaten = vertragsdatenService.findAllByPNAndStatusesNotInVertragsdatenaenderungen(teilnehmer.getPersonalnummer(), List.of(MitarbeiterStatus.ACTIVE, MitarbeiterStatus.VALIDATED, MitarbeiterStatus.NOT_VALIDATED)).stream().findFirst().orElse(null);
                if (vertragsdaten != null && vertragsdaten.getId() != null) {
                    gehaltInfo = gehaltInfoService.findByVertragsdatenId(vertragsdaten.getId());
                    arbeitszeitenInfo = arbeitszeitenInfoService.findByVertragsdatenId(vertragsdaten.getId());
                }
            }
            List<SeminarDto> seminarDtos = new ArrayList<>();
            if (teilnehmer.getTeilnehmerSeminars() != null) {
                seminarDtos = teilnehmer.getTeilnehmerSeminars().stream().map(TeilnehmerServiceImpl::toDto).toList();
                SeminarDto seminarDto = seminarDtos.stream().findFirst().orElse(null);
                if (seminarDto != null) {
                    List<SeminarPruefung> seminarPruefung = seminarPruefungService.findAllByTeilnehmerIdAndSeminarId(id, seminarDto.getId());
                    if (!seminarPruefung.isEmpty()) {
                        SeminarPruefungNiveau seminarPruefungNiveau = seminarPruefung.get(0).getNiveau();
                        return TeilnehmerSeminarDto.builder()
                                .teilnehmerDto(toDto(teilnehmer, stammdaten, vertragsdaten, gehaltInfo, arbeitszeitenInfo))
                                .seminarDtos(seminarDtos)
                                .pruefungNiveau(seminarPruefungNiveau.getName())
                                .nationPalKz(teilnehmer.getNation().stream()
                                        .map(Land::getPalKz)
                                        .collect(Collectors.joining(", ")))
                                .ursprungsLandPalKz(Optional.ofNullable(teilnehmer.getUrsprung())
                                        .map(Adresse::getLand)
                                        .map(Land::getPalKz)
                                        .orElse(null))
                                .build();
                    }

                }

            }
            return TeilnehmerSeminarDto.builder()
                    .teilnehmerDto(toDto(teilnehmer, stammdaten, vertragsdaten, gehaltInfo, arbeitszeitenInfo))
                    .seminarDtos(seminarDtos)
                    .build();
        }
        return null;
    }

    @Override
    public Teilnehmer findByPersonalnummerString(String personalnummer) {
        return teilnehmerRepository.findByPersonalnummerString(personalnummer);
    }


    @Transactional(transactionManager = "postgresTransactionManager", readOnly = true) // keeps session open while touching collections
    @Override
    public Teilnehmer findCompleteTnByPersonalnummerString(String personalnummer) {
        Teilnehmer t = teilnehmerRepository.findByPersonalnummerString(personalnummer);
        if (t != null) {
            // touch all lazy collections/relations to initialize them
            t.getErrors().size();
            t.getNation().size();
            t.getTeilnehmerTelefons().size();
            t.getTeilnehmerSeminars().size();
            t.getTeilnehmer2Wunschberufe().size();
            t.getKompetenzen().size();
            t.getPraktika().size();
            t.getAbschluesse().size();

            // also touch lazy @OneToOne / @ManyToOne if needed
            if (t.getAdresse() != null) t.getAdresse().getId();
            if (t.getUrsprung() != null) t.getUrsprung().getId();
            if (t.getPersonalnummer() != null) t.getPersonalnummer().getId();
            if (t.getAnrede() != null) t.getAnrede().getId();
            if (t.getGeschlecht() != null) t.getGeschlecht().getId();
            if (t.getMuttersprache() != null) t.getMuttersprache().getId();
        }
        return t;
    }

    @Override
    public Teilnehmer findByPersonalnummerId(Integer personalnummer) {
        return teilnehmerRepository.findByPersonalnummer_Id(personalnummer);
    }

    @Override
    public void updateHasBisDocument(boolean hasBisDocument, Integer id) {
        teilnehmerRepository.updateTeilnehmerHasBisDocument(hasBisDocument, id);
    }

    @Override
    public List<Teilnehmer> findAllByVornameAndNachnameAndSvNummer(String vorname, String nachname, String svNummer) {
        return teilnehmerRepository.findAllByVornameAndNachnameAndSvNummer(vorname, nachname, svNummer);
    }

    @Override
    public List<Teilnehmer> findAllByVornameAndNachnameAndGeburtsdatum(String vorname, String nachname, LocalDate geburtsdatum) {
        return teilnehmerRepository.findAllByVornameAndNachnameAndGeburtsdatum(vorname, nachname, geburtsdatum);
    }

    private NewParticipantsSummaryDto mapNewParticipantsSummaryDto(Object[] row) {
        return new NewParticipantsSummaryDto(
                parseTimestampSqlToLocalDate((Timestamp) row[0]),
                (Integer) row[1],
                (Integer) row[2],
                (String) row[3],
                (String) row[4],
                (String) row[5],
                (Long) row[6],
                (Long) row[7],
                (Long) row[8]);
    }

    private Page<TeilnehmerFilterSummaryDto> mapTeilnehmerToFilteredSummary(List<Object[]> rows, long totalCount, String sortProperty, Sort.Direction direction, int page, int size) {
        if (rows == null || rows.isEmpty()) {
            return Page.empty();
        }

        List<TeilnehmerFilterSummaryDto> result = new ArrayList<>();
        for (Object[] row : rows) {
            TeilnehmerFilterSummaryDto teilnehmerFilterSummaryDto = new TeilnehmerFilterSummaryDto(
                    (Integer) row[0],                    // id
                    (String) row[1],                     // vorname
                    (String) row[2],                     // nachname
                    row[3] != null ? Arrays.asList(((String) row[3]).split(",")) : new ArrayList<>(), // massnahmennummern
                    row[4] != null ? Arrays.asList(((String) row[4]).split(",")) : new ArrayList<>(), // seminarNamen
                    (String) row[5],                       // svn
                    (String) row[6],                     // land
                    (String) row[7],                     // ort
                    row[8] != null ? String.valueOf(row[8]) : null,                     // plz
                    row[9] != null ? Arrays.asList(((String) row[9]).split(",")) : new ArrayList<>(), // angemeldetIn
                    row[10] != null && (Boolean) row[10],// isUeba
                    row[11] != null ? TeilnehmerStatus.getNameByCode(((Short) row[11]).intValue()) : null, // status
                    row[12] != null ? Arrays.asList(((String) row[12]).split(",")) : new ArrayList<>() // errors
            );
            result.add(teilnehmerFilterSummaryDto);

        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
        return new PageImpl<>(result, pageable, totalCount);
    }


    public static SeminarDto toDto(Teilnehmer2Seminar teilnehmer2Seminar) {
        if (teilnehmer2Seminar != null) {
            SeminarDto dto = new SeminarDto();
            if (teilnehmer2Seminar.getSeminar() != null) {
                Seminar seminar = teilnehmer2Seminar.getSeminar();
                dto.setId(seminar.getId());
                dto.setProjektId(seminar.getProject().getProjektNummer());
                dto.setProjektName(seminar.getProject().getBezeichnung());
                dto.setSeminarBezeichnung(seminar.getBezeichnung());
                dto.setSeminarNumber(seminar.getSeminarNummer());
                if (seminar.getProject() != null) {
                    dto.setKostentraegerDisplayName(seminar.getProject().getKostentraegerDisplayName());
                }
                dto.setStandort(seminar.getStandort());
                dto.setSchieneUhrzeit(seminar.getSchieneUhrzeit());

                if (seminar.getTrainerSeminars() != null) {
                    List<TrainerDto> trainerList = seminar.getTrainerSeminars().stream()
                            .map(trainerSeminar -> {
                                Benutzer trainer = trainerSeminar.getTrainer();

                                return new TrainerDto(
                                        trainer.getId(),
                                        trainer.getFirstName() + " " + trainer.getLastName(),
                                        trainer.getEmail(),
                                        trainerSeminar.getTrainerFunktion(),
                                        "",
                                        trainerSeminar.getStartDate() != null ? trainerSeminar.getStartDate().toString() : null,
                                        trainerSeminar.getEndDate() != null ? trainerSeminar.getEndDate().toString() : null
                                );
                            })
                            .collect(Collectors.toList());
                    dto.setTrainer(trainerList);
                }

                if (seminar.getZeiterfassungen() != null) {
                    List<SeminarTeilnehmerAbwesenheitDto> abwesenheiten = seminar.getZeiterfassungen().stream()
                            .filter(abwesenheit -> abwesenheit.getZeiterfassungReason() != null
                                    && !abwesenheit.getZeiterfassungReason().getAnwesend())
                            .map(abwesenheit -> {
                                ZeiterfassungReason zeiterfassungReason = abwesenheit.getZeiterfassungReason();
                                AnAbwesenheitKategorieDto abwesenheitKategorieDto = new AnAbwesenheitKategorieDto(zeiterfassungReason.getId(), zeiterfassungReason.getBezeichnung(), zeiterfassungReason.getShortBezeichnung());

                                return new SeminarTeilnehmerAbwesenheitDto(
                                        abwesenheit.getId(),
                                        abwesenheitKategorieDto,
                                        abwesenheit.getDatum().toString(),
                                        abwesenheit.getBemerkung()
                                );
                            })
                            .collect(Collectors.toList());

                    dto.setAbwesenheiten(abwesenheiten);

                    List<SeminarTeilnehmerAnwesenheitDto> anwesenheiten = seminar.getZeiterfassungen().stream()
                            .filter(anwesenheit -> anwesenheit.getZeiterfassungReason() != null
                                    && anwesenheit.getZeiterfassungReason().getAnwesend())
                            .map(anwesenheit -> {
                                ZeiterfassungReason zeiterfassungReason = anwesenheit.getZeiterfassungReason();
                                AnAbwesenheitKategorieDto abwesenheitKategorieDto = new AnAbwesenheitKategorieDto(zeiterfassungReason.getId(), zeiterfassungReason.getBezeichnung(), zeiterfassungReason.getShortBezeichnung());

                                return new SeminarTeilnehmerAnwesenheitDto(
                                        anwesenheit.getId(),
                                        abwesenheitKategorieDto,
                                        anwesenheit.getDatum().toString()
                                );
                            })
                            .collect(Collectors.toList());

                    dto.setAnwesenheiten(anwesenheiten);
                }
                if (seminar.getStartDate() != null) {
                    dto.setKursDatumVon(seminar.getStartDate().toString());
                }
                if (seminar.getEndDate() != null) {
                    dto.setKursDatumBis(seminar.getEndDate().toString());
                }
            }

            if (teilnehmer2Seminar.getAustrittsgrund() != null) {
                dto.setAustrittsgrund(teilnehmer2Seminar.getAustrittsgrund().getName());
            }

            if (teilnehmer2Seminar.getBegehrenBis() != null) {
                dto.setBegehrenBis(teilnehmer2Seminar.getBegehrenBis().toString());
            }
            dto.setZusaetzlicheUnterstuetzung(teilnehmer2Seminar.getZusaetzlicheUnterstuetzung());

            dto.setLernfortschritt(teilnehmer2Seminar.getLernfortschritt());

            if (teilnehmer2Seminar.getFruehwarnung() != null) {
                dto.setFruehwarnung(teilnehmer2Seminar.getFruehwarnung().toString());
            }

            if (teilnehmer2Seminar.getAnteilAnwesenheit() != null) {
                dto.setAnteilAnwesenheit(teilnehmer2Seminar.getAnteilAnwesenheit().toString());
            }

            if (teilnehmer2Seminar.getSeminarGesamtbeurteilung() != null) {
                if (teilnehmer2Seminar.getSeminarGesamtbeurteilung().getType() != null) {
                    dto.setGesamtbeurteilungTyp(teilnehmer2Seminar.getSeminarGesamtbeurteilung().getType().getName());
                }
                if (teilnehmer2Seminar.getSeminarGesamtbeurteilung().getErgebnis() != null) {
                    dto.setGesamtbeurteilungErgebnis(teilnehmer2Seminar.getSeminarGesamtbeurteilung().getErgebnis().getName());
                }
            }

            dto.setBuchungsstatus(teilnehmer2Seminar.getBuchungsstatus());
            dto.setAnmerkung(teilnehmer2Seminar.getAnmerkung());

            if (teilnehmer2Seminar.getGeplant() != null) {
                dto.setGeplant(teilnehmer2Seminar.getGeplant().toString());
            }

            if (teilnehmer2Seminar.getEintritt() != null) {
                dto.setEintritt(teilnehmer2Seminar.getEintritt().toString());
            }

            if (teilnehmer2Seminar.getAustritt() != null) {
                dto.setAustritt(teilnehmer2Seminar.getAustritt().toString());
            }

            dto.setMassnahmennummer(teilnehmer2Seminar.getMassnahmennummer());
            dto.setVeranstaltungsnummer(teilnehmer2Seminar.getVeranstaltungsnummer());

            if (teilnehmer2Seminar.getZubuchung() != null) {
                dto.setZubuchung(teilnehmer2Seminar.getZubuchung().toString());
            }

            if (teilnehmer2Seminar.getRgs() != null) {
                dto.setRgs(String.valueOf(teilnehmer2Seminar.getRgs().getRgs()));
                dto.setRgsBezeichnung(teilnehmer2Seminar.getRgs().getBezeichnung());
            }

            if (teilnehmer2Seminar.getBetreuer() != null) {
                dto.setBetreuerTitel(teilnehmer2Seminar.getBetreuer().getTitel());
                dto.setBetreuerVorname(teilnehmer2Seminar.getBetreuer().getVorname());
                dto.setBetreuerNachname(teilnehmer2Seminar.getBetreuer().getNachname());
            }

            return dto;
        }
        return null;
    }

    public static TeilnehmerDto toDto(Teilnehmer teilnehmer,
                                      Stammdaten stammdaten,
                                      Vertragsdaten vertragsdaten,
                                      GehaltInfo gehaltInfo,
                                      ArbeitszeitenInfo arbeitszeitenInfo) {
        if (teilnehmer == null) {
            return null;
        }
        TeilnehmerDto dto = new TeilnehmerDto();
        dto.setId(teilnehmer.getId());
        dto.setTitel(teilnehmer.getTitel());
        dto.setTitel2(teilnehmer.getTitel2());
        dto.setNachname(teilnehmer.getNachname());
        dto.setVorname(teilnehmer.getVorname());
        dto.setStatus(teilnehmer.getStatus().name());
        if (teilnehmer.getPersonalnummer() != null && !isNullOrBlank(teilnehmer.getPersonalnummer().getPersonalnummer())) {
            dto.setPersonalnummer(teilnehmer.getPersonalnummer().getPersonalnummer());
        }
        if (teilnehmer.getGeschlecht() != null) {
            dto.setGeschlecht(teilnehmer.getGeschlecht().getName());
        }
        if (teilnehmer.getSvNummer() != null) {
            dto.setSvNummer(teilnehmer.getSvNummer().toString());
        }
        if (!teilnehmer.getTelefons().isEmpty()) {
            dto.setTelefon(String.valueOf(teilnehmer.getTelefons().get(0).getTelefonnummer()));
        }
        if (teilnehmer.getGeburtsdatum() != null) {
            dto.setGeburtsdatum(teilnehmer.getGeburtsdatum().toString());
        }
        dto.setEmail(teilnehmer.getEmail());
        dto.setNation(teilnehmer.getNation().stream().map(Land::getLandName).collect(Collectors.joining(", ")));
        if ((teilnehmer.getAdresse() != null)) {
            if (teilnehmer.getAdresse().getPlz() != null) {
                dto.setPlz(teilnehmer.getAdresse().getPlz().getPlzString());
            }
            dto.setOrt(teilnehmer.getAdresse().getOrt());
            dto.setStrasse(teilnehmer.getAdresse().getStrasse());
            dto.setLand(teilnehmer.getAdresse().getLand() != null ? teilnehmer.getAdresse().getLand().getLandName() : null);
        }
        dto.setUeba(teilnehmer.isUeba());

        for (TeilnehmerDataStatus dataStatus : teilnehmer.getErrors()) {
            dto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
        }
        dto.setErrors(new ArrayList<>(dto.getErrorsMap().keySet()));
        if (teilnehmer.getAnrede() != null) {
            dto.setAnrede(teilnehmer.getAnrede().getName());
        }
        dto.setHasBisDocument(teilnehmer.isHasBisDocument());
        if (teilnehmer.getUrsprung() != null) {
            if (teilnehmer.getUrsprung().getLand() != null) {
                dto.setUrsprungsland(teilnehmer.getUrsprung().getLand().getLandName());
            }
            dto.setGeburtsort(teilnehmer.getUrsprung().getOrt());
        }

        dto.setZiel(teilnehmer.getZiel());

        if (teilnehmer.getVermittelbarAb() != null) {
            dto.setVermittelbarAb(teilnehmer.getVermittelbarAb().toString());
        }

        dto.setVermittlungsnotiz(teilnehmer.getVermittlungsNotiz());

        if (teilnehmer.getTeilnehmer2Wunschberufe() != null) {
            List<String> wunschberufe = teilnehmer.getTeilnehmer2Wunschberufe().stream()
                    .map(t2w -> t2w.getWunschberuf().getName())
                    .toList();
            dto.setWunschberufe(wunschberufe);
        }

        if (teilnehmer.getKompetenzen() != null) {
            List<String> kompetenzen = teilnehmer.getKompetenzen().stream()
                    .map(Kompetenz::getName)
                    .collect(Collectors.toList());
            dto.setKompetenzen(kompetenzen);
        }

        if (stammdaten != null) {
            TeilnehmerStammdatenDto teilnehmerStammdatenDto = getTeilnehmerStammdatenDto(stammdaten);
            dto.setStammdaten(teilnehmerStammdatenDto);
        }
        if (vertragsdaten != null) {
            TeilnehmerVertragsdatenDto teilnehmerVertragsdatenDto = getTeilnehmerVertragsdatenDto(vertragsdaten, gehaltInfo, arbeitszeitenInfo);
            dto.setVertragsdaten(teilnehmerVertragsdatenDto);
        }
        if (teilnehmer.getMuttersprache() != null) {
            dto.setMuttersprache(teilnehmer.getMuttersprache().getName());
        }

        // laut #13695 nicht nutzen, braucht noch Analyse
       /* if (teilnehmer.getPraktika() != null) {
            List<TnPraktikaDto> praktika = teilnehmer.getPraktika().stream()
                    .map(praktikum -> new TnPraktikaDto(
                            praktikum.getId(),
                            praktikum.getStartDate().toString(),
                            praktikum.getEndDate().toString(),
                            praktikum.getErprobung(),
                            praktikum.getErgebnis(),
                            praktikum.getNotiz()

                    ))
                    .collect(Collectors.toList());

            dto.setPraktika(praktika);
        }

        if (teilnehmer.getAbschluesse() != null) {
            List<TnAbschlussDto> abschluesse = teilnehmer.getAbschluesse().stream()
                    .map(abschluss -> new TnAbschlussDto(
                            abschluss.getId(),
                            abschluss.getAbschlussAm().toString(),
                            abschluss.getNotiz()

                    ))
                    .collect(Collectors.toList());

            dto.setAbschluesse(abschluesse);
        }*/

        return dto;
    }

    private static TeilnehmerStammdatenDto getTeilnehmerStammdatenDto(Stammdaten stammdaten) {
        TeilnehmerStammdatenDto teilnehmerStammdatenDto = new TeilnehmerStammdatenDto();
        teilnehmerStammdatenDto.setId(stammdaten.getId());
        if (stammdaten.getBank() != null) {
            if (stammdaten.getBank().getBank() != null) {
                teilnehmerStammdatenDto.setBank(stammdaten.getBank().getBank());
            }
            if (stammdaten.getBank().getIban() != null) {
                teilnehmerStammdatenDto.setIban(stammdaten.getBank().getIban());
            }
            if (stammdaten.getBank().getBic() != null) {
                teilnehmerStammdatenDto.setBic(stammdaten.getBank().getBic());
            }
            if (stammdaten.getBank().getCard() != null) {
                teilnehmerStammdatenDto.setBankcard(stammdaten.getBank().getCard().getValue());
            }
        }
        for (StammdatenDataStatus dataStatus : stammdaten.getErrors()) {
            String error = dataStatus.getError();
            if ("iban".equalsIgnoreCase(error) || "bank".equalsIgnoreCase(error)
                    || "bic".equalsIgnoreCase(error) || "bankcard".equalsIgnoreCase(error)) {
                teilnehmerStammdatenDto.getErrorsMap().put(dataStatus.getError(), dataStatus.getCause());
            }
        }
        teilnehmerStammdatenDto.setErrors(new ArrayList<>(teilnehmerStammdatenDto.getErrorsMap().keySet()));
        return teilnehmerStammdatenDto;
    }

    private static TeilnehmerVertragsdatenDto getTeilnehmerVertragsdatenDto(Vertragsdaten vertragsdaten, GehaltInfo gehaltInfo, ArbeitszeitenInfo arbeitszeitenInfo) {
        TeilnehmerVertragsdatenDto teilnehmerVertragsdatenDto = new TeilnehmerVertragsdatenDto();
        setVertragsdatenDtoBasics(vertragsdaten, teilnehmerVertragsdatenDto);
        setVertragsdatenDtoGehalt(gehaltInfo, teilnehmerVertragsdatenDto);
        setVertragsdatenDtoArbeitszeiten(arbeitszeitenInfo, teilnehmerVertragsdatenDto);
        return teilnehmerVertragsdatenDto;
    }

    private static void setVertragsdatenDtoBasics(Vertragsdaten vertragsdaten, TeilnehmerVertragsdatenDto teilnehmerVertragsdatenDto) {
        teilnehmerVertragsdatenDto.setId(vertragsdaten.getId());
        if (vertragsdaten.getEintritt() != null) {
            teilnehmerVertragsdatenDto.setEintritt(vertragsdaten.getEintritt().toString());
        }
        if (vertragsdaten.getIsBefristet() != null) {
            teilnehmerVertragsdatenDto.setIsBefristet(vertragsdaten.getIsBefristet());
        }
        if (vertragsdaten.getBefristungBis() != null) {
            teilnehmerVertragsdatenDto.setBefristungBis(vertragsdaten.getBefristungBis().toString());
        }
        if (vertragsdaten.getDienstort() != null) {
            teilnehmerVertragsdatenDto.setDienstort(vertragsdaten.getDienstort().getName());
        }
        if (vertragsdaten.getKostenstelle() != null) {
            teilnehmerVertragsdatenDto.setKostenstelle(vertragsdaten.getKostenstelle().getBezeichnung());
        }
        if (vertragsdaten.getKategorie() != null) {
            teilnehmerVertragsdatenDto.setKategorie(vertragsdaten.getKategorie().getName());
        }
        if (vertragsdaten.getTaetigkeit() != null) {
            teilnehmerVertragsdatenDto.setTaetigkeit(vertragsdaten.getTaetigkeit().getName());
        }
        if (vertragsdaten.getNotizAllgemein() != null) {
            teilnehmerVertragsdatenDto.setNotizAllgemein(vertragsdaten.getNotizAllgemein());
        }
        if (vertragsdaten.getDienstnehmergruppe() != null) {
            teilnehmerVertragsdatenDto.setDienstnehmergruppe(vertragsdaten.getDienstnehmergruppe().getBezeichnung());
        }
        if (vertragsdaten.getAbrechnungsgruppe() != null) {
            teilnehmerVertragsdatenDto.setAbrechnungsgruppe(vertragsdaten.getAbrechnungsgruppe().getBezeichnung());
        }
        if (vertragsdaten.getKlasse() != null) {
            teilnehmerVertragsdatenDto.setKlasse(vertragsdaten.getKlasse().getName());
        }
    }

    private static void setVertragsdatenDtoGehalt(GehaltInfo gehaltInfo, TeilnehmerVertragsdatenDto teilnehmerVertragsdatenDto) {
        if (gehaltInfo != null) {
            if (gehaltInfo.getVerwendungsgruppe() != null && gehaltInfo.getVerwendungsgruppe().getKollektivvertrag() != null) {
                teilnehmerVertragsdatenDto.setKollektivvertrag(gehaltInfo.getVerwendungsgruppe().getKollektivvertrag().getName());
            }
            if (gehaltInfo.getLehrjahr() != null) {
                teilnehmerVertragsdatenDto.setLehrjahr(gehaltInfo.getLehrjahr());
            }
            if (gehaltInfo.getNaechsteVorrueckung() != null) {
                teilnehmerVertragsdatenDto.setNaechsteVorrueckung(gehaltInfo.getNaechsteVorrueckung());
            }
        }
    }

    private static void setVertragsdatenDtoArbeitszeiten(ArbeitszeitenInfo arbeitszeitenInfo, TeilnehmerVertragsdatenDto teilnehmerVertragsdatenDto) {
        if (arbeitszeitenInfo != null) {
            if (arbeitszeitenInfo.getWochenstunden() != null) {
                teilnehmerVertragsdatenDto.setWochenstunden(arbeitszeitenInfo.getWochenstunden());
            }
        }
    }

    @Override
    public List<SimpleTNDto> findAllByAbgemeldetenTeilnehmer(AbmeldungStatus status) {
        return teilnehmerRepository.findAllByAbgemeldetenTeilnehmer(status).stream().map(this::tnToSimpleTN).collect(Collectors.toList());
    }

    private SimpleTNDto tnToSimpleTN(Teilnehmer teilnehmer) {
        SimpleTNDto simpleTNDto = new SimpleTNDto();
        if (!isNullOrBlank(teilnehmer.getVorname())) {
            simpleTNDto.setVorname(teilnehmer.getVorname());
        }
        if (!isNullOrBlank(teilnehmer.getNachname())) {
            simpleTNDto.setNachname(teilnehmer.getNachname());
        }
        if (teilnehmer.getSvNummer() != null) {
            simpleTNDto.setSvn(teilnehmer.getSvNummer().toString());
        }
        if (teilnehmer.getGeburtsdatum() != null) {
            simpleTNDto.setGeburtsdatum(teilnehmer.getGeburtsdatum().toString());
        }
        return simpleTNDto;
    }

    @Override
    public List<Teilnehmer> findTeilnehmersWithIds(List<Integer> ids) {
        return teilnehmerRepository.findByIdIn(ids);
    }

    @Override
    public Teilnehmer saveAndFlush(Teilnehmer teilnehmer) {
        return teilnehmerRepository.saveAndFlush(teilnehmer);
    }
}

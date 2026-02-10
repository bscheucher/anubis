package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.AbwesenheitTaetigkeitenType;
import com.ibosng.dbservice.dtos.ZeitausgleichDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenType;
import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Zeitausgleich;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.repositories.ZeitausgleichRepository;
import com.ibosng.dbservice.services.BenutzerService;
import com.ibosng.dbservice.services.ZeitausgleichService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import com.ibosng.dbservice.utils.Parsers;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Helpers.localDateToString;
import static com.ibosng.dbservice.utils.Parsers.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ZeitausgleichServiceImpl implements ZeitausgleichService {

    private final ZeitausgleichRepository zeitausgleichRepository;
    private final PersonalnummerService personalnummerService;
    private final BenutzerService benutzerService;
    private final UrlaubsdatenService urlaubsdatenService;
    private final StammdatenService stammdatenService;

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Zeitausgleich> findAll() {
        return zeitausgleichRepository.findAll();
    }

    @Override
    public Optional<Zeitausgleich> findById(Integer id) {
        return zeitausgleichRepository.findById(id);
    }

    @Override
    public Zeitausgleich save(Zeitausgleich object) {
        return zeitausgleichRepository.save(object);
    }

    @Override
    public List<Zeitausgleich> saveAll(List<Zeitausgleich> objects) {
        return zeitausgleichRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeitausgleichRepository.deleteById(id);
    }

    @Override
    public List<Zeitausgleich> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public ZeitausgleichDto map(Zeitausgleich zeitausgleich) {
        ZeitausgleichDto zeitausgleichDto = new ZeitausgleichDto();
        if (zeitausgleich.getId() != null) {
            zeitausgleichDto.setId(zeitausgleich.getId());
        }
        if (zeitausgleich.getPersonalnummer() != null) {
            zeitausgleichDto.setPersonalnummer(zeitausgleich.getPersonalnummer().getPersonalnummer());
        }
        zeitausgleichDto.setId(zeitausgleich.getId());
        if (zeitausgleich.getDatum() != null) {
            zeitausgleichDto.setDatum(localDateToString(zeitausgleich.getDatum()));
        }
        if (zeitausgleich.getTimeBis() != null) {
            zeitausgleichDto.setTimeBis(zeitausgleich.getTimeBis().format(DateTimeFormatter.ISO_LOCAL_TIME));
        }
        if (zeitausgleich.getTimeVon() != null) {
            zeitausgleichDto.setTimeVon(zeitausgleich.getTimeVon().format(DateTimeFormatter.ISO_LOCAL_TIME));
        }
        if (zeitausgleich.getTimeVon() != null && zeitausgleich.getTimeBis() != null) {
            Duration duration = Duration.between(zeitausgleich.getTimeVon(), zeitausgleich.getTimeBis());
            zeitausgleichDto.setDurationHours((double) duration.toMinutes() / 60.0);
        } else {
            zeitausgleichDto.setDurationHours(0.0);
        }
        if (!isNullOrBlank(zeitausgleich.getComment())) {
            zeitausgleichDto.setComment(zeitausgleich.getComment());
        }
        if (zeitausgleich.getCreatedOn() != null) {
            zeitausgleichDto.setCreatedOn(zeitausgleich.getCreatedOn().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        // Map fuehrungskraefte
        if (zeitausgleich.getFuehrungskraefte() != null && !zeitausgleich.getFuehrungskraefte().isEmpty()) {
            zeitausgleich.getFuehrungskraefte().forEach(fuehrungskraft -> zeitausgleichDto.getFuehrungskraefte().add(fuehrungskraft.getEmail()));
        }
        return zeitausgleichDto;
    }

    @Override
    public AbwesenheitDto mapZeitausgleichToAbwesenheitDto(Zeitausgleich zeitausgleich) {
        AbwesenheitDto abwesenheitDto = new AbwesenheitDto();
        abwesenheitDto.setType(AbwesenheitType.ZEITAUSGLEICH);
        if (zeitausgleich.getId() != null) {
            abwesenheitDto.setId(zeitausgleich.getId());
        }
        if (zeitausgleich.getPersonalnummer() != null) {
            abwesenheitDto.setPersonalnummerId(zeitausgleich.getPersonalnummer().getId());
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerId(zeitausgleich.getPersonalnummer().getId());
            if (stammdaten != null) {
                StringBuilder sb = new StringBuilder();
                if (!isNullOrBlank(stammdaten.getVorname())) {
                    sb.append(stammdaten.getVorname());
                }
                if (!isNullOrBlank(stammdaten.getNachname()) && !isNullOrBlank(stammdaten.getVorname())) {
                    sb.append(" ");
                }
                if (!isNullOrBlank(stammdaten.getNachname())) {
                    sb.append(stammdaten.getNachname());
                }
                abwesenheitDto.setFullName(sb.toString());
            }
        }
        abwesenheitDto.setId(zeitausgleich.getId());
        if (zeitausgleich.getDatum() != null) {
            abwesenheitDto.setStartDate(zeitausgleich.getDatum());
            abwesenheitDto.setEndDate(zeitausgleich.getDatum());
        }
        if (zeitausgleich.getStatus() != null) {
            abwesenheitDto.setStatus(zeitausgleich.getStatus());
        }
        if (!isNullOrBlank(zeitausgleich.getComment())) {
            abwesenheitDto.setComment(zeitausgleich.getComment());
        }
        // Map fuehrungskraefte
        if (zeitausgleich.getFuehrungskraefte() != null && !zeitausgleich.getFuehrungskraefte().isEmpty()) {
            zeitausgleich.getFuehrungskraefte().forEach(fuehrungskraft -> abwesenheitDto.getFuehrungskraefte().add(fuehrungskraft.getEmail()));
        }
        if (zeitausgleich.getChangedOn() != null) {
            abwesenheitDto.setChangedOn(LocalDate.from(zeitausgleich.getChangedOn()));
        } else {
            abwesenheitDto.setChangedOn(LocalDate.from(zeitausgleich.getCreatedOn()));
        }
        if (!isNullOrBlank(zeitausgleich.getCommentFuehrungskraft())) {
            abwesenheitDto.setCommentFuehrungskraft(zeitausgleich.getCommentFuehrungskraft());
        }
        abwesenheitDto.setLhrCalculated(true);

        urlaubsdatenService.findUrlaubsdatenByPersonalnummerMonth(abwesenheitDto.getPersonalnummerId(), abwesenheitDto.getEndDate())
                .ifPresentOrElse(
                        value -> abwesenheitDto.setAnspruch(value.getAnspruch()),
                        () -> abwesenheitDto.setAnspruch(0d));

        return abwesenheitDto;
    }

    @Override
    public ZeitbuchungenDto mapZeitausgleichToZeitbuchungenDto(Zeitausgleich zeitausgleich) {
        ZeitbuchungenDto zeitbuchungenDto = new ZeitbuchungenDto();
        zeitbuchungenDto.setAnAbwesenheit(ZeitbuchungenType.ABWESENHEIT);
        zeitbuchungenDto.setVon(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).format(DateTimeFormatter.ISO_LOCAL_TIME));
        zeitbuchungenDto.setBis(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).format(DateTimeFormatter.ISO_LOCAL_TIME));
        zeitbuchungenDto.setLeistungsdatum(zeitausgleich.getDatum() != null ? zeitausgleich.getDatum().format(DateTimeFormatter.ISO_LOCAL_DATE) : null);
        zeitbuchungenDto.setDauerStd(zeitausgleich.getTimeVon() != null && zeitausgleich.getTimeBis() != null ? Duration.between(zeitausgleich.getTimeVon(), zeitausgleich.getTimeBis()).toMinutes() / 60.0 : null);
        zeitbuchungenDto.setJahr(zeitausgleich.getDatum() != null ? zeitausgleich.getDatum().getYear() : null);
        zeitbuchungenDto.setMonat(zeitausgleich.getDatum() != null ? zeitausgleich.getDatum().getMonthValue() : null);
        zeitbuchungenDto.setTaetigkeit(AbwesenheitTaetigkeitenType.ZEITAUSGLEICH.getLabel());
        return zeitbuchungenDto;
    }

    @Override
    public List<AbwesenheitDto> mapListZeitausgleichToListAbwesenheitDto(List<Zeitausgleich> zeitausgleichList) {
        List<AbwesenheitDto> sortedList = zeitausgleichList.stream().map(this::mapZeitausgleichToAbwesenheitDto)
                .sorted(
                        Comparator
                                .comparing(AbwesenheitDto::getPersonalnummerId, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(AbwesenheitDto::getComment, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(AbwesenheitDto::getStatus, Comparator.nullsFirst(Comparator.naturalOrder()))
                                .thenComparing(AbwesenheitDto::getStartDate, Comparator.nullsFirst(Comparator.naturalOrder()))
                ).toList();
        List<AbwesenheitDto> mergedList = new ArrayList<>();
        if (sortedList.isEmpty()) {
            return mergedList; // Return empty if the list is empty
        }

        // Initialize the first range
        AbwesenheitDto previous = sortedList.get(0);
        AbwesenheitDto current = null;
        boolean wasMerged = false;
        for (int i = 1; i < sortedList.size(); i++) {
            current = sortedList.get(i);
            if (!current.getStartDate().minusDays(1).equals(previous.getEndDate()) ||
                    !Objects.equals(current.getStatus(), previous.getStatus()) ||
                    !Objects.equals(current.getComment(), previous.getComment())) {
                mergedList.add(previous);
                previous = current;
                wasMerged = sortedList.size() != i + 1;
            } else {
                wasMerged = false;
            }
            previous.setId(current.getId());
            previous.setFullName(current.getFullName());
            previous.setCommentFuehrungskraft(current.getCommentFuehrungskraft());
            if (current.getAnspruch() == null) {
                previous.setAnspruch(0d);
            } else {
                previous.setAnspruch(current.getAnspruch());
            }
            if (!isNullOrBlank(current.getComment())) {
                previous.setComment(current.getComment());
            }
            previous.setEndDate(current.getEndDate());
        }
        if (!wasMerged) {
            mergedList.add(previous);
        }

        mergedList.forEach(entity -> entity.setDurationInDays(
                String.valueOf(ChronoUnit.DAYS.between(entity.getStartDate(), entity.getEndDate().plusDays(1)))
        ));
        return mergedList;
    }

    @Override
    public List<ZeitbuchungenDto> mapListZeitausgleichToListZeitbuchungenDto(List<Zeitausgleich> zeitausgleichList) {
        return zeitausgleichList.stream().map(this::mapZeitausgleichToZeitbuchungenDto)
                .sorted(
                        Comparator
                                .comparing(ZeitbuchungenDto::getPersonalnummer, Comparator.nullsFirst(Comparator.naturalOrder()))
                ).toList();
    }


    @Override
    public Zeitausgleich map(ZeitausgleichDto zeitausgleichDto) {
        Zeitausgleich zeitausgleich = new Zeitausgleich();
        if (zeitausgleichDto.getId() != null) {
            zeitausgleich.setId(zeitausgleichDto.getId());
        }
        Personalnummer personalnummer = personalnummerService.findByPersonalnummer(zeitausgleichDto.getPersonalnummer());
        if (personalnummer != null) {
            zeitausgleich.setPersonalnummer(personalnummer);
        }

        if (!isNullOrBlank(zeitausgleichDto.getComment())) {
            zeitausgleich.setComment(zeitausgleichDto.getComment());
        }
        if (isValidDate(zeitausgleichDto.getDatum())) {
            zeitausgleich.setDatum(parseDate(zeitausgleichDto.getDatum()));
        }
        if (isValidTime(zeitausgleichDto.getTimeBis())) {
            zeitausgleich.setTimeBis(parseTime(zeitausgleichDto.getTimeBis()));
        }
        if (isValidTime(zeitausgleichDto.getTimeVon())) {
            zeitausgleich.setTimeVon(parseTime(zeitausgleichDto.getTimeVon()));
        }
        // Map fuehrungskraefte
        if (zeitausgleichDto.getFuehrungskraefte() != null && !zeitausgleichDto.getFuehrungskraefte().isEmpty()) {
            Set<Benutzer> fuehrungskraefteSet = zeitausgleichDto.getFuehrungskraefte().stream()
                    .map(benutzerService::findByEmail)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            zeitausgleich.setFuehrungskraefte(fuehrungskraefteSet);
        } else {
            zeitausgleich.setFuehrungskraefte(Collections.emptySet());
        }

        return zeitausgleich;
    }

    @Override
    public boolean isExist(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis) {
        return zeitausgleichRepository.existsByPersonalnummer_IdAndDatumAndTimeVonAndTimeBis(
                personalnummerId, datum, timeVon, timeBis);
    }

    @Override
    public Zeitausgleich findByPersonalnummerAndDateTime(Integer personalnummerId, LocalDate datum, LocalTime timeVon, LocalTime timeBis) {
        return zeitausgleichRepository.findFirstByPersonalnummer_IdAndDatumAndTimeVonAndTimeBis(
                personalnummerId, datum, timeVon, timeBis);
    }

    @Override
    public Zeitausgleich findByPersonalnummerAndDateTimeVon(Integer personalnummerId, LocalDate datum, LocalTime timeVon) {
        return zeitausgleichRepository.findByPersonalnummer_IdAndDatumAndTimeVon(personalnummerId, datum, timeVon);
    }

    @Override
    public List<Zeitausgleich> findByPersonalnummerAndDate(Integer personalnummerId, LocalDate date) {
        return zeitausgleichRepository.findByPersonalnummer_IdAndDatum(personalnummerId, date);
    }

    @Override
    public Page<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate dateVon, LocalDate dateBis, Pageable pageable) {
        return zeitausgleichRepository.findByPersonalnummer_IdAndDatumBetweenOrderByDatumDescCreatedOnDesc(personalnummerId, dateVon, dateBis, pageable);
    }

    @Override
    public List<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate dateVon, LocalDate dateBis) {
        return zeitausgleichRepository.findByPersonalnummer_IdAndDatumBetweenOrderByDatumDescCreatedOnDesc(personalnummerId, dateVon, dateBis);
    }

    @Override
    public List<Zeitausgleich> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate dateVon, LocalDate dateBis, List<AbwesenheitStatus> abwesenheitStatuses) {
        return zeitausgleichRepository.findByPersonalnummer_IdAndDatumBetweenAndStatusIn(personalnummerId, dateVon, dateBis, abwesenheitStatuses);
    }

    @Override
    public List<Zeitausgleich> findAllByFuehrungskraefteId(Integer id) {
        return zeitausgleichRepository.findAllByFuehrungskraefte_Id(id);
    }

    @Override
    public List<Zeitausgleich> findAllByPersonalnummerPersonalnummer(Integer personalnummerId) {
        return zeitausgleichRepository.findAllByPersonalnummer_Id(personalnummerId);
    }

    @Override
    public List<Zeitausgleich> findAllZeitausgleichInPeriod(int anyZeitausgleichId) {
        Zeitausgleich zeitausgleichFirst = zeitausgleichRepository.findFirstDayInZeitausgleich(anyZeitausgleichId);
        Zeitausgleich zeitausgleichLast = zeitausgleichRepository.findLastDayInZeitausgleich(anyZeitausgleichId);

        return zeitausgleichRepository.findZeitausgleichInPeriod(zeitausgleichFirst.getId(), zeitausgleichLast.getId());
    }

    @Override
    public List<Zeitausgleich> findAllByIdIn(List<Integer> ids) {
        return zeitausgleichRepository.findAllByIdIn(ids);
    }

    @Override
    @Transactional(value = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Optional<Zeitausgleich> findByIdAndForceRefresh(Integer id) {
        zeitausgleichRepository.findById(id).ifPresent(this::refresh);
        return zeitausgleichRepository.findById(id);
    }

    @Override
    public List<Zeitausgleich> findAllByFuehrungskraefteIdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList) {
        return zeitausgleichRepository.findAllByFuehrungskraefte_IdAndStatusIn(id, statusList);
    }

    private void refresh(Zeitausgleich entity) {
        entityManager.unwrap(Session.class).refresh(entity);
    }

    @Override
    public List<Zeitausgleich> findFilteredZeitausgleichByStatusAndYear(Integer personalnummerId, String status, Integer year) {
        if (isNullOrBlank(status) && year == null) {
            return findAllByPersonalnummerPersonalnummer(personalnummerId);
        } else if (isNullOrBlank(status) && year != null) {
            return zeitausgleichRepository.findAllByPersonalnummerPersonalnummerAndVonYear(personalnummerId, year);
        } else if (!isNullOrBlank(status) && year == null) {
            return zeitausgleichRepository.findAllByPersonalnummer_IdAndStatus(personalnummerId, AbwesenheitStatus.valueOf(status));
        } else if (!isNullOrBlank(status) && year != null) {
            return zeitausgleichRepository.findAllByPersonalnummerPersonalnummerAndStatusAndVonYear(personalnummerId, year, AbwesenheitStatus.valueOf(status));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Zeitausgleich> findFilteredZeitausgleichByStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, String startDate, String endDate) {
        LocalDate startLocalDate = Parsers.parseDate(startDate);
        LocalDate endLocalDate = Parsers.parseDate(endDate);
        return zeitausgleichRepository.findAllByPersonalnummerAndStatusInPeriod(personalnummerId, excludedStatuses, startLocalDate, endLocalDate);
    }

    @Override
    public List<String> findDistinctYearsByPersonalnummer(Integer personalnummerId) {
        return zeitausgleichRepository.findDistinctYearsByPersonalnummer(personalnummerId).stream().map(String::valueOf).toList();
    }
}

package com.ibosng.dbservice.services.impl.lhr;

import com.ibosng.dbservice.dtos.mitarbeiter.AbwesenheitDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.AbwesenheitDetailedDto;
import com.ibosng.dbservice.dtos.urlaubsdaten.AbwesenheitOverviewDto;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.lhr.AbwesenheitStatus;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.AbwesenheitType;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.dbservice.services.lhr.AbwesenheitService;
import com.ibosng.dbservice.services.mitarbeiter.PersonalnummerService;
import com.ibosng.dbservice.services.mitarbeiter.StammdatenService;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import com.ibosng.dbservice.utils.Parsers;
import de.jollyday.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.hibernate.Session;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Constants.*;
import static com.ibosng.dbservice.utils.Helpers.isWeekend;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@RequiredArgsConstructor
@Service
public class AbwesenheitServiceImpl implements AbwesenheitService {

    private final static String ANSPRUCH_TYPE_NORMAL = "Normalurlaub";

    private final AbwesenheitRespository abwesenheitRespository;
    private final UrlaubsdatenService urlaubsdatenService;
    private final PersonalnummerService personalnummerService;
    private final StammdatenService stammdatenService;
    private HolidayManager holidayManager;

    @PostConstruct
    public void init() {
        ManagerParameter parameter = ManagerParameters.create(HolidayCalendar.AUSTRIA);
        this.holidayManager = HolidayManager.getInstance(parameter);
    }


    @PersistenceContext
    private final EntityManager entityManager;

    private void refresh(Abwesenheit entity) {
        entityManager.unwrap(Session.class).refresh(entity);
    }

    @Override
    public List<Abwesenheit> findAll() {
        return abwesenheitRespository.findAll();
    }

    @Override
    public Optional<Abwesenheit> findById(Integer id) {
        return abwesenheitRespository.findById(id);
    }


    @Override
    @Transactional(value = "postgresTransactionManager", propagation = Propagation.REQUIRES_NEW)
    public Optional<Abwesenheit> findByIdAndForceRefresh(Integer id) {
        abwesenheitRespository.findById(id).ifPresent(this::refresh);
        return abwesenheitRespository.findById(id);
    }

    @Override
    public Abwesenheit save(Abwesenheit object) {
        return abwesenheitRespository.save(object);
    }

    @Override
    public List<Abwesenheit> saveAll(List<Abwesenheit> objects) {
        return abwesenheitRespository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        abwesenheitRespository.deleteFuehrungskraefteRelations(id);
        abwesenheitRespository.deleteById(id);
    }

    @Override
    public List<Abwesenheit> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public AbwesenheitDto mapToAbwesenheitDto(Abwesenheit abwesenheit) {
        if (abwesenheit == null) {
            return null;
        }
        AbwesenheitDto dto = new AbwesenheitDto();
        dto.setId(abwesenheit.getId());
        if (!isNullOrBlank(abwesenheit.getGrund())) {
            dto.setType(EnumUtils.getEnum(AbwesenheitType.class, abwesenheit.getGrund()));
        }
        dto.setComment(abwesenheit.getKommentar());
        if (abwesenheit.getPersonalnummer() != null) {
            dto.setPersonalnummerId(abwesenheit.getPersonalnummer().getId());
            Stammdaten stammdaten = stammdatenService.findByPersonalnummerId(abwesenheit.getPersonalnummer().getId());
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
                dto.setFullName(sb.toString());
            }
        }

        if (abwesenheit.getTage() != null && abwesenheit.getTage() != 0) {
            dto.setDurationInDays(String.valueOf(Math.round(abwesenheit.getTage())));
            dto.setLhrCalculated(true);
        } else if (abwesenheit.getVon() != null && abwesenheit.getBis() != null) {
            dto.setDurationInDays(String.valueOf(Math.round(calculateBusinessDays(abwesenheit.getVon(), abwesenheit.getBis(), AUSTRIA_AT))));
            dto.setLhrCalculated(false);
        }

        if (abwesenheit.getVon() != null) {
            dto.setStartDate(abwesenheit.getVon());
        }
        if (abwesenheit.getBis() != null) {
            dto.setEndDate(abwesenheit.getBis());
        }
        if (!isNullOrBlank(abwesenheit.getCommentFuehrungskraft())) {
            dto.setCommentFuehrungskraft(abwesenheit.getCommentFuehrungskraft());
        }
        dto.setAnspruch(abwesenheit.getVerbaucht());
        dto.setStatus(abwesenheit.getStatus());
        dto.setChangedOn(abwesenheit.getChangedOn() != null ? abwesenheit.getChangedOn().toLocalDate() : abwesenheit.getCreatedOn().toLocalDate());

        // Map fuehrungskraefte
        if (abwesenheit.getFuehrungskraefte() != null && !abwesenheit.getFuehrungskraefte().isEmpty()) {
            abwesenheit.getFuehrungskraefte().forEach(fuehrungskraft -> dto.getFuehrungskraefte().add(fuehrungskraft.getEmail()));
        }
        dto.setLhrHttpStatus(abwesenheit.getLhrHttpStatus());
        return dto;
    }

    @Override
    public List<Abwesenheit> findAllByPersonalnummer(Integer personalnummerId) {
        return abwesenheitRespository.findAllByPersonalnummer_Id(personalnummerId);
    }

    @Override
    public List<Abwesenheit> findAllByFuehrungskraefteId(Integer benutzerId) {
        return abwesenheitRespository.findAllByFuehrungskraefte_Id(benutzerId);
    }

    @Override
    public Abwesenheit findByPersonalnummerVonAndBis(Integer personalnummerId, LocalDate von, LocalDate bis) {
        return abwesenheitRespository.findFirstByPersonalnummer_IdAndVonAndBis(personalnummerId, von, bis);
    }


    @Override
    public AbwesenheitOverviewDto formUrlaubOverview(Integer personalnummerId, LocalDate von, LocalDate bis, Pageable pageable) {
        List<Urlaubsdaten> urlaubsdatenList = urlaubsdatenService.findByPersonalnummerInPeriodAndAnspruchType(personalnummerId, von.withDayOfMonth(1), bis, ANSPRUCH_TYPE_NORMAL);
        AbwesenheitOverviewDto abwesenheitOverviewDto = new AbwesenheitOverviewDto();

        if (urlaubsdatenList != null && !urlaubsdatenList.isEmpty()) {
            Urlaubsdaten overview = urlaubsdatenList.get(0);
            Urlaubsdaten anspruchOverview = urlaubsdatenList.get(urlaubsdatenList.size() - 1);
            abwesenheitOverviewDto = AbwesenheitOverviewDto.builder()
                    .rest(Math.round(overview.getRest()))
                    .verbraucht(Math.round(anspruchOverview.getAnspruch()))
                    .konsum(Math.round(urlaubsdatenList.stream().mapToDouble(Urlaubsdaten::getKonsum).sum()))
                    .build();

            if (overview.getNextAnspruch() != null) {
                abwesenheitOverviewDto.setNextAnspruch(overview.getNextAnspruch().format(DateTimeFormatter.ISO_DATE));
            }

            if (overview.getMonth() != null) {
                abwesenheitOverviewDto.setMonth(overview.getMonth().format(DateTimeFormatter.ISO_DATE));
            }
        }

        List<AbwesenheitDetailedDto> abwesenheitenList = abwesenheitRespository.findByDateRangeGrundAndStatus(personalnummerId, von, bis, List.of(AbwesenheitStatus.VALID, AbwesenheitStatus.ACCEPTED, AbwesenheitStatus.USED), List.of(URLAUB, ARZT, KRANK), pageable).stream().map(this::mapToAbwesenheitDetailedDto).toList();

        abwesenheitOverviewDto.setUrlaube(abwesenheitenList);

        return abwesenheitOverviewDto;
    }


    @Override
    public long countByPersonalnummerBeweenVonAndBis(Integer personalnummerId, LocalDate von, LocalDate bis) {
        return abwesenheitRespository.countByPersonalnummer_IdAndVonAfterAndBisBefore(personalnummerId, von, bis);
    }

    @Override
    public long countByPersonalnummerBetweenVonAndBisAndStatus(Integer personalnummerId, LocalDate von, LocalDate bis, List<AbwesenheitStatus> status) {
        return abwesenheitRespository.countByPersonalnummer_IdAndVonAfterAndBisBeforeAndStatusIn(personalnummerId, von, bis, status);
    }

    @Override
    public long countByPersonalnummerBetweenVonAndBisStatusAndGrunde(Integer personalnummerId, LocalDate von, LocalDate bis, List<AbwesenheitStatus> status, List<String> grunde) {
        return abwesenheitRespository.countByPersonalnummer_IdAndVonAfterAndBisBeforeAndStatusInAndGrundIn(personalnummerId, von, bis, status, grunde);
    }

    @Override
    public AbwesenheitDetailedDto mapToAbwesenheitDetailedDto(Abwesenheit abwesenheit) {
        if (abwesenheit == null) {
            return null;
        }
        AbwesenheitDetailedDto dto = AbwesenheitDetailedDto.builder()
                .days((abwesenheit.getTage() == null || abwesenheit.getTage() == 0) ? Math.round(calculateBusinessDays(abwesenheit.getVon(), abwesenheit.getBis(), AUSTRIA_AT)) : abwesenheit.getTage())
                .saldo(abwesenheit.getSaldo())
                .isLhrCalculated(!(abwesenheit.getTage() == null || abwesenheit.getTage() == 0))
                .comment(abwesenheit.getKommentar())
                .build();
        if (!isNullOrBlank(abwesenheit.getGrund())) {
            AbwesenheitType abwesenheitType = EnumUtils.getEnum(AbwesenheitType.class, abwesenheit.getGrund());
            if (abwesenheitType != null) {
                dto.setUrlaubType(abwesenheitType.getValue());
            }
        }
        if (abwesenheit.getStatus() != null) {
            dto.setStatus(abwesenheit.getStatus().name());
        }
        if (abwesenheit.getVon() != null) {
            dto.setStartDate(abwesenheit.getVon().format(DateTimeFormatter.ISO_DATE));
        }
        if (abwesenheit.getBis() != null) {
            dto.setEndDate(abwesenheit.getBis().format(DateTimeFormatter.ISO_DATE));
        }
        return dto;
    }

    @Override
    public Abwesenheit map(AbwesenheitDto abwesenheitDto, AbwesenheitStatus status) {
        Abwesenheit abwesenheit = map(abwesenheitDto);
        if (abwesenheit != null) {
            abwesenheit.setStatus(status);
        }
        return abwesenheit;
    }

    @Override
    public Abwesenheit map(AbwesenheitDto abwesenheitDto) {
        if (abwesenheitDto == null) {
            return null;
        }
        Abwesenheit abwesenheit = null;
        if (abwesenheitDto.getId() != null) {
            abwesenheit = findById(abwesenheitDto.getId()).orElse(null);
        }
        if (abwesenheit == null) {
            abwesenheit = new Abwesenheit();
        }
        if (abwesenheitDto.getType() != null) {
            abwesenheit.setGrund(abwesenheitDto.getType().toString());
        }
        if (abwesenheitDto.getPersonalnummerId() != null) {
            Personalnummer personalnummer = personalnummerService.findById(abwesenheitDto.getPersonalnummerId()).orElse(null);
            if (personalnummer != null) {
                abwesenheit.setPersonalnummer(personalnummer);
            }
        }
        if (!isNullOrBlank(abwesenheitDto.getDurationInDays())) {
            abwesenheit.setTage(Double.parseDouble(abwesenheitDto.getDurationInDays()));
        }
        if (!isNullOrBlank(abwesenheitDto.getComment())) {
            abwesenheit.setKommentar(abwesenheitDto.getComment());
        }
        abwesenheit.setVon(abwesenheitDto.getStartDate());
        abwesenheit.setBis(abwesenheitDto.getEndDate());
        abwesenheit.setStatus(abwesenheitDto.getStatus());
        return abwesenheit;
    }

    @Override
    public List<Abwesenheit> findAllByFuehrungskraefteIdAndStatusIn(Integer id, List<AbwesenheitStatus> statusList) {
        return abwesenheitRespository.findAllByFuehrungskraefte_IdAndStatusIn(id, statusList);
    }

    @Override
    public List<Abwesenheit> findFilteredAbwesenheitenByStatusAndYear(Integer personalnummerId, String status, Integer year) {
        if (isNullOrBlank(status) && year == null) {
            return findAllByPersonalnummer(personalnummerId);
        } else if (isNullOrBlank(status) && year != null) {
            return abwesenheitRespository.findAllByPersonalnummerPersonalnummerAndVonYear(personalnummerId, year);
        } else if (!isNullOrBlank(status) && year == null) {
            return abwesenheitRespository.findAllByPersonalnummer_IdAndStatus(personalnummerId, AbwesenheitStatus.valueOf(status));
        } else if (!isNullOrBlank(status) && year != null) {
            return abwesenheitRespository.findAllByPersonalnummerPersonalnummerAndStatusAndVonYear(personalnummerId, year, AbwesenheitStatus.valueOf(status));
        }
        return new ArrayList<>();
    }

    @Override
    public List<Abwesenheit> findFilteredAbwesenheitenByStatusInPeriod(Integer personalnummerId, List<AbwesenheitStatus> excludedStatuses, String startDate, String endDate) {
        LocalDate startLocalDate = Parsers.parseDate(startDate);
        LocalDate endLocalDate = Parsers.parseDate(endDate);
        return abwesenheitRespository.findAllByPersonalnummerAndStatusInPeriod(personalnummerId, excludedStatuses, startLocalDate, endLocalDate);
    }

    @Override
    public List<String> findDistinctYearsByPersonalnummer(Integer personalnummerId) {
        return abwesenheitRespository.findDistinctYearsByPersonalnummer(personalnummerId).stream().map(String::valueOf).toList();
    }

    @Override
    public List<Abwesenheit> findAllUncalculatedAbwesenheit() {
        List<AbwesenheitStatus> status = List.of(AbwesenheitStatus.ACCEPTED, AbwesenheitStatus.ACCEPTED_FINAL, AbwesenheitStatus.VALID);
        return abwesenheitRespository.findByTageNullOrSaldoNullAndStatusIn(status);
    }

    private Set<LocalDate> getHolidaysForCountry(LocalDate startDate, LocalDate endDate, String country) {
        Set<Holiday> holidaySet = holidayManager.getHolidays(startDate.getYear(), country);

        return holidaySet.stream().map(Holiday::getDate).filter(holiday -> !holiday.isBefore(startDate) && !holiday.isAfter(endDate)).collect(Collectors.toSet());
    }

    @Override
    public long calculateBusinessDays(LocalDate startDate, LocalDate endDate, String country) {
        endDate = endDate.plusDays(1);
        Set<LocalDate> holidays = getHolidaysForCountry(startDate, endDate, country);
        return startDate.datesUntil(endDate).filter(date -> !isWeekend(date)).filter(date -> !holidays.contains(date)).count();
    }

    @Override
    public List<Abwesenheit> findAbwesenheitBetweenDates(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate) {
        return abwesenheitRespository.findAbwesenheitBetweenDates(personalnummerId, vonDate, bisDate);
    }

    @Override
    public List<Abwesenheit> findAbwesenheitBetweenDatesAndStatuses(Integer personalnummerId, LocalDate vonDate, LocalDate bisDate, List<AbwesenheitStatus> abwesenheitStatuses) {
        return abwesenheitRespository.findAbwesenheitBetweenDatesAndStatusesIn(personalnummerId, vonDate, bisDate, abwesenheitStatuses);
    }

    @Override
    public List<Personalnummer> findAbwesenheitPersonalnummerFromDateAndStatusesIn(LocalDate date, List<AbwesenheitStatus> statuses) {
        return abwesenheitRespository.findAbwesenheitPersonalnummerFromDateAndStatusesIn(date, statuses);
    }

}

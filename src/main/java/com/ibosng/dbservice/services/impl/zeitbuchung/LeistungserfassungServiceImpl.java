package com.ibosng.dbservice.services.impl.zeitbuchung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungstyp;
import com.ibosng.dbservice.entities.zeitbuchung.MoxisStatus;
import com.ibosng.dbservice.repositories.zeitbuchung.LeistungserfassungRepository;
import com.ibosng.dbservice.services.zeitbuchung.LeistungserfassungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;
import static com.ibosng.dbservice.utils.Parsers.parseDate;

@Service
@RequiredArgsConstructor
public class LeistungserfassungServiceImpl implements LeistungserfassungService {
    private final LeistungserfassungRepository leistungserfassungRepository;

    @Override
    public List<Leistungserfassung> findAll() {
        return leistungserfassungRepository.findAll();
    }

    @Override
    public Optional<Leistungserfassung> findById(Integer id) {
        return leistungserfassungRepository.findById(id);
    }

    @Override
    public Leistungserfassung save(Leistungserfassung object) {
        return leistungserfassungRepository.save(object);
    }

    @Override
    public List<Leistungserfassung> saveAll(List<Leistungserfassung> objects) {
        return leistungserfassungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        leistungserfassungRepository.deleteById(id);
    }

    @Override
    public List<Leistungserfassung> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Leistungserfassung findByLeistungstypLeistungsdatumAndPersonalnummer(Leistungstyp leistungstyp, LocalDate leistungsdatum, Personalnummer personalnummer) {
        return leistungserfassungRepository.findFirstByLeistungstypAndLeistungsdatumAndPersonalnummer(leistungstyp, leistungsdatum, personalnummer).orElse(null);
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerInPeriod(Personalnummer personalnummer, String startDate, String endDate) {
        final LocalDate startLocalDate = parseDate(startDate);
        final LocalDate endLocalDate = parseDate(endDate);
        return leistungserfassungRepository.findByPersonalnummerAndLeistungsdatumBetween(personalnummer, startLocalDate, endLocalDate);
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerAndDate(Personalnummer personalnummer, String date) {
        final LocalDate localDate = parseDate(date);
        return leistungserfassungRepository.findByPersonalnummerAndLeistungsdatum(personalnummer ,localDate);
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerAndMonth(Personalnummer personalnummer, LocalDate localDate) {
        final LocalDate startDate = localDate.withDayOfMonth(1);
        final LocalDate endDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
        return leistungserfassungRepository.findByPersonalnummerAndLeistungsdatumBetween(personalnummer, startDate, endDate);
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerAndMonthClosed(Integer personalnummerId, LocalDate localDate) {
        final LocalDate startDate = localDate.withDayOfMonth(1);
        final LocalDate endDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
        return leistungserfassungRepository.findByPersonalNummerMonthsToBeSend(personalnummerId, startDate, endDate, MoxisStatus.SUCCESS);
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerAndMonthClosedDistinct(Integer personalnummerId, LocalDate localDate) {
        final LocalDate startDate = localDate.withDayOfMonth(1);
        final LocalDate endDate = localDate.withDayOfMonth(localDate.lengthOfMonth());
        List<Leistungserfassung> leistungserfassungsList = leistungserfassungRepository.findByPersonalNummerMonthsToBeSend(personalnummerId, startDate, endDate, MoxisStatus.SUCCESS);

        return new ArrayList<>(leistungserfassungsList.stream()
                .collect(Collectors.toMap(
                        Leistungserfassung::getLeistungsdatum,
                        leistung -> leistung,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    @Override
    public List<Leistungserfassung> getSyncedLeistungserfassungenInPeriod(Integer personalnummerId, LocalDate startDate, LocalDate endDate, boolean isSynced) {
        if (isSynced) {
            return leistungserfassungRepository.findByPersonalnummer_IdAndLeistungsdatumBetweenAndIsSyncedWithLhrTrue(personalnummerId, startDate, endDate);
        } else {
            return leistungserfassungRepository.findByPersonalnummer_IdAndLeistungsdatumBetweenAndIsSyncedWithLhrNullOrIsSyncedWithLhrFalse(personalnummerId, startDate, endDate);
        }
    }

    @Override
    public List<Leistungserfassung> findLeistungserfassungInPeriod(LocalDate startDate, LocalDate endDate) {
        return leistungserfassungRepository.findLeistungserfassungInPeriodAndNotInStatus(startDate, endDate, MoxisStatus.SUCCESS);
    }

    @Override
    public List<LocalDate> findDatesWithOverlapping(Integer personalnummerId, String startDate, String endDate) {
        if (isNullOrBlank(startDate) || isNullOrBlank(endDate)) {
            return List.of();
        }
        LocalDate startLocalDate = parseDate(startDate);
        LocalDate endLocalDate = parseDate(endDate);
        if (startLocalDate == null || endLocalDate == null) {
            return List.of();
        }
        return leistungserfassungRepository.findOverlapingInPeriod(personalnummerId, startLocalDate, endLocalDate)
                .stream().map(Date::toLocalDate).toList();
    }

    @Override
    public List<Leistungserfassung> findByPersonalnummerAndDateAndLeistungstype(Integer personalnummerId, String leistungsdatum, Leistungstyp leistungstyp) {
        final LocalDate localDate = parseDate(leistungsdatum);
        return leistungserfassungRepository.findByPersonalnummer_IdAndLeistungsdatumAndLeistungstyp(personalnummerId, localDate, leistungstyp);
    }

    @Override
    public boolean isLeistungserfassungMonthClosed(Integer personalnummerId, Integer bmdClient, LocalDate localDate) {
        LocalDate startDate = localDate.withDayOfMonth(1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return leistungserfassungRepository.existsByPersonalnummer_IdAndPersonalnummer_Firma_BmdClientAndLeistungsdatumBetweenAndIsLocked(personalnummerId, bmdClient, startDate, endDate, true);
    }

    @Override
    public boolean isLeistungserfassungMonthClosedMoxis(Integer personalnummerId, Integer bmdClient, LocalDate localDate) {
        LocalDate startDate = localDate.withDayOfMonth(1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return leistungserfassungRepository
                .existsByPersonalnummer_IdAndPersonalnummer_Firma_BmdClientAndLeistungsdatumBetweenAndMoxisStatusIn(personalnummerId, bmdClient, startDate, endDate, List.of(MoxisStatus.SUCCESS));
    }

    @Override
    public List<Leistungserfassung> findAllNotSyncedWithLhr() {
        return leistungserfassungRepository.findByIsSyncedWithLhrNullOrIsSyncedWithLhrFalse();
    }
}

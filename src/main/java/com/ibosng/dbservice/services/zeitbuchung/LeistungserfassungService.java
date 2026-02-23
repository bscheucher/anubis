package com.ibosng.dbservice.services.zeitbuchung;

import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungstyp;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.util.List;

public interface LeistungserfassungService extends BaseService<Leistungserfassung> {
    Leistungserfassung findByLeistungstypLeistungsdatumAndPersonalnummer(Leistungstyp leistungstyp, LocalDate leistungsdatum, Personalnummer personalnummer);

    List<Leistungserfassung> findByPersonalnummerInPeriod(Personalnummer personalnummer, String startDate, String endDate);

    List<Leistungserfassung> findByPersonalnummerAndDate(Personalnummer personalnummer, String date);

    List<Leistungserfassung> findByPersonalnummerAndMonth(Personalnummer personalnummer, LocalDate localDate);

    List<Leistungserfassung> findByPersonalnummerAndMonthClosed(Integer personalnummerId, LocalDate localDate);

    List<Leistungserfassung> findByPersonalnummerAndMonthClosedDistinct(Integer personalnummerId, LocalDate localDate);

    List<Leistungserfassung> getSyncedLeistungserfassungenInPeriod(Integer personnalnummerId, LocalDate startDate, LocalDate endDate, boolean isSynced);

    List<Leistungserfassung> findLeistungserfassungInPeriod(LocalDate startDate, LocalDate endDate);

    List<LocalDate> findDatesWithOverlapping(Integer personalnummerId, String startDate, String endDate);

    List<Leistungserfassung> findByPersonalnummerAndDateAndLeistungstype(Integer personalnummerId, String leistungsdatum, Leistungstyp leistungstyp);

    boolean isLeistungserfassungMonthClosed(Integer personalnummerId, Integer bmdClient, LocalDate localDate);

    boolean isLeistungserfassungMonthClosedMoxis(Integer personalnummerId, Integer bmdClient, LocalDate localDate);

    List<Leistungserfassung> findAllNotSyncedWithLhr();
}

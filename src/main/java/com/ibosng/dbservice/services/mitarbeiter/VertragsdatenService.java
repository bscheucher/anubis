package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.MitarbeiterDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VertragsdatenService extends BaseService<Vertragsdaten> {
    List<Vertragsdaten> findByPersonalnummerString(String personalnummer);

    List<Vertragsdaten> findByPersonalnummerStringAndStatus(String personalnummer, List<MitarbeiterStatus> status);

    List<Vertragsdaten> findByPersonalnummerIdAndStatus(Integer personalnummerId, List<MitarbeiterStatus> status);

    Vertragsdaten findByPersonalnummerStringLatest(String personalnummer);

    VertragsdatenDto mapVertragsdatenToDto(Vertragsdaten vertragsdaten);

    MitarbeiterDto mapStammdatenToMADto(Stammdaten stammdaten, Vertragsdaten vertragsdaten);

    LocalDate findEintrittByPersonalnummer(String personalnummer);

    List<Vertragsdaten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after);

    List<Vertragsdaten> findAllByFuehrungskraftIsNullAndFuehrungskraftRefIsNotNull();

    List<Vertragsdaten> findAllByStartcoachIsNullAndStartcoachRefIsNotNull();

    @Deprecated(since = "Use the method where the Personalnummer as object is used")
    List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(String personalnummer, List<MitarbeiterStatus> statuses);

    List<Vertragsdaten> findAllByPNAndStatusesNotInVertragsdatenaenderungen(Personalnummer personalnummer, List<MitarbeiterStatus> statuses);

    List<Vertragsdaten> findAllByPersonalnummer(Personalnummer personalnummer);
}

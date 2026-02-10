package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.MAFilteredResultDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MASearchCriteriaDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MitarbeiterSummaryDto;
import com.ibosng.dbservice.dtos.mitarbeiter.StammdatenDto;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface StammdatenService extends BaseService<Stammdaten> {

    Stammdaten findByPersonalnummer(Personalnummer personalnummer);

    Stammdaten findByPersonalnummerId(Integer personalnummerId);

    Stammdaten findByPersonalnummerString(String personalnummer);
    Stammdaten findByPersonalnummerStringAndStatusIn(String personalnummer, List<MitarbeiterStatus> status);

    Stammdaten findByPersonalnummerAndStatusIn(Personalnummer personalnummer, List<MitarbeiterStatus> status);

    Page<MAFilteredResultDto> findMAByCriteria(MASearchCriteriaDto maSearchCriteriaDto, Pageable pageable);

    StammdatenDto mapStammdatenToDto(Stammdaten stammdaten);

    Page<Stammdaten> findAll(Pageable pageable);

    Page<MitarbeiterSummaryDto> findAllOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String wwgName);
    Page<MitarbeiterSummaryDto> findForBenutzerOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String wwgName, String benutzerEmail);

    List<Stammdaten> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after);

    List<Stammdaten> findAllByVornameAndNachname(String vorname, String nachname);

    List<Stammdaten> findAllByEmail(String email);
}

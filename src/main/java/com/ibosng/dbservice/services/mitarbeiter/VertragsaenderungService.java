package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.dtos.changelog.FieldChangeDto;
import com.ibosng.dbservice.dtos.changelog.VertragsaenderungChangeLogDto;
import com.ibosng.dbservice.dtos.mitarbeiter.VertragsdatenDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungBasicDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungDto;
import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungOverviewDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.mitarbeiter.MitarbeiterStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsaenderung;
import com.ibosng.dbservice.entities.mitarbeiter.VertragsaenderungStatus;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VertragsaenderungService extends BaseService<Vertragsaenderung> {
    List<Vertragsaenderung> findAllByPersonalnummerAndStatus(String personalnummer, VertragsaenderungStatus vertragsaenderungStatus);

    List<Vertragsaenderung> findAllByPersonalnummer_Personalnummer(String personalnummer);

    List<Vertragsaenderung> findAllByPersonalnummer_PersonalnummerAndStatus(String personalnummer, MitarbeiterStatus status);

    List<Vertragsaenderung> findAllBySuccessor_Id(Integer successorId);

    List<Vertragsaenderung> findAllByPredecessor_Id(Integer predecessorId);

    Vertragsaenderung findBySuccessor_IdAndPredecessor_Id(Integer successorId, Integer predecessorId);

    Page<VertragsaenderungOverviewDto> findAllOrderedAndFilteredForOverview(String searchTerm, List<String> statuses, Pageable pageable);

    VertragsaenderungDto mapToVetragsaenderungDto(Vertragsaenderung vertragsaenderung);

    VertragsaenderungChangeLogDto getVertragsaenderungenChangeLog(String personalnummerString);

    VertragsaenderungBasicDto mapToVetragsaenderungBasicDto(Vertragsaenderung vertragsaenderung);

    Vertragsaenderung updateVertragsaenderung(VertragsaenderungDto vertragsaenderungDto);

    List<FieldChangeDto> compareVertragsdaten(VertragsdatenDto vertragsdatenOld, VertragsdatenDto vertragsdatenNew);

    Vertragsaenderung map(VertragsaenderungDto vertragsaenderungDto, Benutzer antragssteller, Vertragsdaten oldVertragsdaten, Vertragsdaten newVertragsdaten);

    Vertragsaenderung setEmails(Vertragsaenderung vertragsaenderung, VertragsaenderungDto vertragsaenderungDto);
}

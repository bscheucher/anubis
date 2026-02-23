package com.ibosng.dbservice.services.impl.zeitbuchung;

import com.ibosng.dbservice.dtos.AbwesenheitTaetigkeitenType;
import com.ibosng.dbservice.dtos.ZeitbuchungenDto;
import com.ibosng.dbservice.dtos.ZeitbuchungenType;
import com.ibosng.dbservice.entities.lhr.Abwesenheit;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungserfassung;
import com.ibosng.dbservice.entities.zeitbuchung.Leistungsort;
import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchung;
import com.ibosng.dbservice.repositories.lhr.AbwesenheitRespository;
import com.ibosng.dbservice.repositories.zeitbuchung.ZeitbuchungRepository;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZeitbuchungServiceImpl implements ZeitbuchungService {

    private final ZeitbuchungRepository zeitbuchungRepository;
    private final AbwesenheitRespository abwesenheitRespository;

    @Override
    public List<Zeitbuchung> findAll() {
        return zeitbuchungRepository.findAll();
    }

    @Override
    public Optional<Zeitbuchung> findById(Integer id) {
        return zeitbuchungRepository.findById(id);
    }

    @Override
    public Zeitbuchung save(Zeitbuchung object) {
        return zeitbuchungRepository.save(object);
    }

    @Override
    public List<Zeitbuchung> saveAll(List<Zeitbuchung> objects) {
        return zeitbuchungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeitbuchungRepository.deleteById(id);
    }

    @Override
    public List<Zeitbuchung> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<Zeitbuchung> getZeitbuchungenByListungserfassen(Leistungserfassung leistungserfassung) {
        return zeitbuchungRepository.findAllByLeistungserfassung(leistungserfassung);
    }

    @Override
    public boolean isZeitbuchungExists(LocalTime von, LocalTime bis, Boolean anAbwesenheit, Leistungsort leistungsort,
                                       Integer seminarId, Integer zeitbuchuntypId, Integer leistungserfassungId, Integer kostenstelleId) {

        return zeitbuchungRepository.findFirstByVonAndBisAndAnAbwesenheitAndLeistungsortAndSeminar_IdAndZeitbuchungstyp_IdAndLeistungserfassung_IdAndKostenstelle_Id(
                von, bis, anAbwesenheit, leistungsort, seminarId, zeitbuchuntypId, leistungserfassungId, kostenstelleId).isPresent();
    }

    @Override
    public List<Zeitbuchung> findZeitbuchungenInPeriodAndAnAbwesenheit(Integer personalnummerId, LocalDate startDate, LocalDate endDate, Boolean isAnAbwesenheit) {
        return zeitbuchungRepository.findZeitbuchungenInPeriodAndAnAbwesenheit(personalnummerId, startDate, endDate, isAnAbwesenheit);
    }

    @Override
    public ZeitbuchungenDto mapToZeitbuchungenDto(Abwesenheit abwesenheit, LocalDate date) {
        ZeitbuchungenDto zeitbuchungenDto = new ZeitbuchungenDto();
        zeitbuchungenDto.setAnAbwesenheit(ZeitbuchungenType.ABWESENHEIT);
        zeitbuchungenDto.setLeistungsdatum(date.format(DateTimeFormatter.ISO_LOCAL_DATE));
        zeitbuchungenDto.setJahr(date.getYear());
        zeitbuchungenDto.setMonat(date.getMonthValue());
        zeitbuchungenDto.setTaetigkeit(AbwesenheitTaetigkeitenType.URLAUB.getLabel());
        zeitbuchungenDto.setVon(LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT).format(DateTimeFormatter.ISO_LOCAL_TIME));
        zeitbuchungenDto.setBis(LocalDateTime.of(LocalDate.now(), LocalTime.of(23, 59, 59)).format(DateTimeFormatter.ISO_LOCAL_TIME));

        return zeitbuchungenDto;
    }
}

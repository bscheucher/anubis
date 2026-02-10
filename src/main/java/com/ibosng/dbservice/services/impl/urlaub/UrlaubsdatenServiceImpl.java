package com.ibosng.dbservice.services.impl.urlaub;

import com.ibosng.dbservice.dtos.UrlaubsdatenDto;
import com.ibosng.dbservice.entities.urlaub.Urlaubsdaten;
import com.ibosng.dbservice.repositories.urlaub.UrlaubsdatenRepository;
import com.ibosng.dbservice.services.urlaub.UrlaubsdatenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Constants.DATE_PATTERN;

@Service
@RequiredArgsConstructor
public class UrlaubsdatenServiceImpl implements UrlaubsdatenService {
    private final UrlaubsdatenRepository urlaubsdatenRepository;

    @Override
    public List<Urlaubsdaten> findAll() {
        return urlaubsdatenRepository.findAll();
    }

    @Override
    public Optional<Urlaubsdaten> findById(Integer id) {
        return urlaubsdatenRepository.findById(id);
    }

    @Override
    public Urlaubsdaten save(Urlaubsdaten object) {
        return urlaubsdatenRepository.save(object);
    }

    @Override
    public List<Urlaubsdaten> saveAll(List<Urlaubsdaten> objects) {
        return urlaubsdatenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        urlaubsdatenRepository.deleteById(id);
    }

    @Override
    public List<Urlaubsdaten> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public boolean isExists(Integer personalnummerId, Integer anspuruchLhr, LocalDate month, LocalDate from, LocalDate nextAnspuruch) {
        return urlaubsdatenRepository.existsByPersonalnummer_IdAndAnspruchType_LhrIdAndMonthAndFromAndNextAnspruch(
                personalnummerId, anspuruchLhr, month, from, nextAnspuruch);
    }

    @Override
    public Urlaubsdaten findUrlaubsdaten(Integer personalnummerId, Integer anspuruchLhr, LocalDate month, LocalDate from, LocalDate nextAnspuruch) {
        return urlaubsdatenRepository.findFirstByPersonalnummer_IdAndAnspruchType_LhrIdAndMonthAndFromAndNextAnspruch(
                personalnummerId, anspuruchLhr, month, from, nextAnspuruch);
    }

    @Override
    public List<Urlaubsdaten> findByMonth(Integer personalnummerId, LocalDate month) {
        return urlaubsdatenRepository.findByPersonalnummer_IdAndMonth(personalnummerId, month);
    }

    @Override
    public List<Urlaubsdaten> findByPersonalnummerInPeriod(Integer personalnummerId, LocalDate monthStart, LocalDate monthEnd) {
        return urlaubsdatenRepository.findByPersonalnummer_IdAndMonthBetweenOrderByMonthDesc(personalnummerId, monthStart, monthEnd);
    }

    @Override
    public List<Urlaubsdaten> findByPersonalnummerInPeriodAndAnspruchType(Integer personalnummerId, LocalDate month, LocalDate monthEnd, String anspruch) {
        return urlaubsdatenRepository.findByPersonalnummer_IdAndMonthBetweenAndAnspruchType_BezeichnungOrderByMonthDesc(personalnummerId, month, monthEnd, anspruch);
    }

    @Override
    public UrlaubsdatenDto mapToDto(Urlaubsdaten urlaubsdaten) {
        if (urlaubsdaten == null) {
            return null;
        }
        return UrlaubsdatenDto.builder()
                .id(urlaubsdaten.getId())
                .personalnummer(urlaubsdaten.getPersonalnummer() != null ? urlaubsdaten.getPersonalnummer().getPersonalnummer() : null)
                .anspuruch(urlaubsdaten.getAnspruchType() != null ? urlaubsdaten.getAnspruchType().getBezeichnung() : null)
                .month(urlaubsdaten.getMonth() != null ? urlaubsdaten.getMonth()
                        .format(DateTimeFormatter.ofPattern(DATE_PATTERN)) : null)
                .from(urlaubsdaten.getFrom() != null ? urlaubsdaten.getFrom()
                        .format(DateTimeFormatter.ofPattern(DATE_PATTERN)) : null)
                .nextAnspruch(urlaubsdaten.getNextAnspruch() != null ? urlaubsdaten.getNextAnspruch()
                        .format(DateTimeFormatter.ofPattern(DATE_PATTERN)) : null)
                .kuerzung(urlaubsdaten.getKuerzung())
                .verjaehrung(urlaubsdaten.getVerjaehrung())
                .anspruch(urlaubsdaten.getAnspruch())
                .konsum(urlaubsdaten.getKonsum())
                .rest(urlaubsdaten.getRest())
                .build();
    }

    @Override
    public Optional<Urlaubsdaten> findUrlaubsdatenByPersonalnummerMonth(Integer personalnummerId, LocalDate month) {
        month = month.withDayOfMonth(1);
        return urlaubsdatenRepository.findFirstByPersonalnummer_IdAndMonthOrderByAnspruchType_IdAsc(personalnummerId, month);
    }
}

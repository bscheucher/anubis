package com.ibosng.dbservice.services.impl.zeiterfassung;

import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.zeiterfassung.Zeiterfassung;
import com.ibosng.dbservice.entities.zeiterfassung.ZeiterfassungStatus;
import com.ibosng.dbservice.repositories.zeiterfassung.ZeiterfassungRepository;
import com.ibosng.dbservice.services.zeiterfassung.ZeiterfassungService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZeiterfassungServiceImpl implements ZeiterfassungService {
    private final ZeiterfassungRepository zeiterfassungRepository;

    @Override
    public List<Zeiterfassung> findAll() {
        return zeiterfassungRepository.findAll();
    }

    @Override
    public Optional<Zeiterfassung> findById(Integer id) {
        return zeiterfassungRepository.findById(id);
    }

    @Override
    public Zeiterfassung save(Zeiterfassung object) {
        return zeiterfassungRepository.save(object);
    }

    @Override
    public List<Zeiterfassung> saveAll(List<Zeiterfassung> objects) {
        return zeiterfassungRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeiterfassungRepository.deleteById(id);
    }

    @Override
    public List<Zeiterfassung> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Optional<Zeiterfassung> findBySeminarTeilnehmerDatum(Seminar seminar, Teilnehmer teilnehmer, LocalDate datum) {
        return zeiterfassungRepository.findBySeminarAndTeilnehmerAndDatum(seminar, teilnehmer, datum);
    }

    @Override
    public List<Zeiterfassung> findAllByZeiterfassungTransferIdAndStatus(Integer zeiterfassungTransferId, ZeiterfassungStatus status) {
        return zeiterfassungRepository.findAllByZeiterfassungTransferIdAndStatus(zeiterfassungTransferId, status);
    }
}

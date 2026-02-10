package com.ibosng.dbservice.services.impl.natif;

import com.ibosng.dbservice.entities.natif.Kompetenz;
import com.ibosng.dbservice.repositories.natif.KompetenzenRepository;
import com.ibosng.dbservice.services.natif.KompetenzenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KompetenzenServiceImpl implements KompetenzenService {

    private final KompetenzenRepository kompetenzenRepository;

    @Override
    public List<Kompetenz> findAll() {
        return kompetenzenRepository.findAll();
    }

    @Override
    public Optional<Kompetenz> findById(Integer id) {
        return kompetenzenRepository.findById(id);
    }

    @Override
    public Kompetenz save(Kompetenz object) {
        return kompetenzenRepository.save(object);
    }

    @Override
    public List<Kompetenz> saveAll(List<Kompetenz> objects) {
        return kompetenzenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kompetenzenRepository.deleteById(id);
    }

    @Override
    public List<Kompetenz> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public List<Kompetenz> getKompetenzByTeilnehmerId(Integer teilnehmerId) {
        return kompetenzenRepository.findByTeilnehmerId(teilnehmerId);
    }

    @Transactional
    @Override
    public void deleteByTeilnehmerId(Integer teilnehmerId) {
        kompetenzenRepository.deleteByTeilnehmerId(teilnehmerId);
    }

    @Override
    public boolean isExists(Kompetenz kompetenz) {
        return kompetenzenRepository.existsByTeilnehmer_IdAndArtAndNameAndConfidenceNameAndScoreAndConfidenceScoreAndPinCodeAndConfidencePinCodeAndTagesdatumAndConfidenceTagesdatum(
                kompetenz.getTeilnehmer() != null ? kompetenz.getTeilnehmer().getId() : null,
                kompetenz.getArt(), kompetenz.getName(), kompetenz.getConfidenceName(), kompetenz.getScore(),
                kompetenz.getConfidenceScore(), kompetenz.getPinCode(), kompetenz.getConfidencePinCode(),
                kompetenz.getTagesdatum(), kompetenz.getConfidenceTagesdatum());
    }
}

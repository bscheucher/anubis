package com.ibosng.dbservice.services.impl.mitarbeiter;


import com.ibosng.dbservice.dtos.mitarbeiter.UnterhaltsberechtigteDto;
import com.ibosng.dbservice.entities.mitarbeiter.Unterhaltsberechtigte;
import com.ibosng.dbservice.repositories.mitarbeiter.UnterhaltsberechtigteRepository;
import com.ibosng.dbservice.services.mitarbeiter.UnterhaltsberechtigteService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Service
public class UnterhaltsberechtigteServiceImpl implements UnterhaltsberechtigteService {

    private final UnterhaltsberechtigteRepository unterhaltsberechtigteRepository;

    public UnterhaltsberechtigteServiceImpl(UnterhaltsberechtigteRepository unterhaltsberechtigteRepository) {
        this.unterhaltsberechtigteRepository = unterhaltsberechtigteRepository;
    }

    @Override
    public List<Unterhaltsberechtigte> findAll() {
        return unterhaltsberechtigteRepository.findAll();
    }

    @Override
    public Optional<Unterhaltsberechtigte> findById(Integer id) {
        return unterhaltsberechtigteRepository.findById(id);
    }

    @Override
    public Unterhaltsberechtigte save(Unterhaltsberechtigte object) {
        return unterhaltsberechtigteRepository.save(object);
    }

    @Override
    public List<Unterhaltsberechtigte> saveAll(List<Unterhaltsberechtigte> objects) {
        return unterhaltsberechtigteRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        unterhaltsberechtigteRepository.deleteById(id);
    }

    @Override
    public List<Unterhaltsberechtigte> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Unterhaltsberechtigte> findAllByVertragsdatenId(Integer vertragsdatenId) {
        return unterhaltsberechtigteRepository.findAllByVertragsdatenId(vertragsdatenId);
    }

    @Override
    public UnterhaltsberechtigteDto mapUnterhaltsberechtigteToDto(Unterhaltsberechtigte unterhaltsberechtigte) {
        UnterhaltsberechtigteDto unterhaltsberechtigteDto = new UnterhaltsberechtigteDto();
        unterhaltsberechtigteDto.setId(unterhaltsberechtigte.getId());
        if (unterhaltsberechtigte.getVertragsdaten() != null &&
                unterhaltsberechtigte.getVertragsdaten().getPersonalnummer() != null &&
                !isNullOrBlank(unterhaltsberechtigte.getVertragsdaten().getPersonalnummer().getPersonalnummer())
        ) {
            unterhaltsberechtigteDto.setPersonalnummer(unterhaltsberechtigte.getVertragsdaten().getPersonalnummer().getPersonalnummer());
        }
        if (!isNullOrBlank(unterhaltsberechtigte.getVorname())) {
            unterhaltsberechtigteDto.setUVorname(unterhaltsberechtigte.getVorname());
        }
        if (!isNullOrBlank(unterhaltsberechtigte.getNachname())) {
            unterhaltsberechtigteDto.setUNachname(unterhaltsberechtigte.getNachname());
        }
        if (unterhaltsberechtigte.getSvn() != null) {
            unterhaltsberechtigteDto.setUSvnr(String.valueOf(unterhaltsberechtigte.getSvn()));
        }
        if (unterhaltsberechtigte.getGeburtsdatum() != null) {
            unterhaltsberechtigteDto.setUGeburtsdatum(unterhaltsberechtigte.getGeburtsdatum().toString());
        }
        if (unterhaltsberechtigte.getVerwandtschaft() != null) {
            unterhaltsberechtigteDto.setUVerwandtschaft(unterhaltsberechtigte.getVerwandtschaft().getName());
        }
        return unterhaltsberechtigteDto;
    }

    @Override
    public List<Unterhaltsberechtigte> findAllByCreatedOnOrChangedOnAfter(LocalDateTime after) {
        return unterhaltsberechtigteRepository.findAllByCreatedOnAfterOrChangedOnAfter(after, after);
    }
}

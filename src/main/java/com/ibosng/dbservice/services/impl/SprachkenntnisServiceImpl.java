package com.ibosng.dbservice.services.impl;

import com.ibosng.dbservice.dtos.SprachkenntnisDto;
import com.ibosng.dbservice.entities.Sprachkenntnis;
import com.ibosng.dbservice.repositories.masterdata.SprachkenntnisRepository;
import com.ibosng.dbservice.services.SprachkenntnisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprachkenntnisServiceImpl implements SprachkenntnisService {

    private final SprachkenntnisRepository sprachkenntnisRepository;

    @Override
    public List<Sprachkenntnis> findAll() {
        return sprachkenntnisRepository.findAll();
    }

    @Override
    public Optional<Sprachkenntnis> findById(Integer id) {
        return sprachkenntnisRepository.findById(id);
    }

    @Override
    public Sprachkenntnis save(Sprachkenntnis object) {
        return sprachkenntnisRepository.save(object);
    }

    @Override
    public List<Sprachkenntnis> saveAll(List<Sprachkenntnis> objects) {
        return sprachkenntnisRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        sprachkenntnisRepository.deleteById(id);
    }

    @Override
    public List<Sprachkenntnis> findAllByIdentifier(String identifier) {
        return null;
    }


    public List<Sprachkenntnis> findAllByTeilnehmerId(Integer teilnehmerId) {
        return sprachkenntnisRepository.findAllByTeilnehmerId(teilnehmerId);
    }

    public SprachkenntnisDto mapTeilnehmerSprachkenntnisToDto(Sprachkenntnis sprachkenntnis) {
        SprachkenntnisDto sprachkenntnisDto = new SprachkenntnisDto();
        if (sprachkenntnis != null) {
            sprachkenntnisDto.setId(sprachkenntnis.getId());
            sprachkenntnisDto.setSprache(sprachkenntnis.getSprache() != null ? sprachkenntnis.getSprache().getName() : null);
            sprachkenntnisDto.setNiveau(sprachkenntnis.getSpracheNiveau() != null ? sprachkenntnis.getSpracheNiveau().getName() : null);
            sprachkenntnisDto.setBewertungCoach(sprachkenntnis.getBewertungCoach() != null ? sprachkenntnis.getBewertungCoach().getName() : null);
            sprachkenntnisDto.setBewertungDatum(sprachkenntnis.getBewertungDatum() != null ? sprachkenntnis.getBewertungDatum().toString() : null);
        }
        return sprachkenntnisDto;
    }
}

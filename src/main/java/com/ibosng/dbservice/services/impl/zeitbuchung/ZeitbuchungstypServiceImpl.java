package com.ibosng.dbservice.services.impl.zeitbuchung;

import com.ibosng.dbservice.entities.zeitbuchung.Zeitbuchungstyp;
import com.ibosng.dbservice.repositories.zeitbuchung.ZeitbuchungstypRepository;
import com.ibosng.dbservice.services.zeitbuchung.ZeitbuchungstypService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZeitbuchungstypServiceImpl implements ZeitbuchungstypService {

    private final ZeitbuchungstypRepository zeitbuchungstypRepository;

    @Override
    public List<Zeitbuchungstyp> findAll() {
        return zeitbuchungstypRepository.findAll();
    }

    @Override
    public Optional<Zeitbuchungstyp> findById(Integer id) {
        return zeitbuchungstypRepository.findById(id);
    }

    @Override
    public Zeitbuchungstyp save(Zeitbuchungstyp object) {
        return zeitbuchungstypRepository.save(object);
    }

    @Override
    public List<Zeitbuchungstyp> saveAll(List<Zeitbuchungstyp> objects) {
        return zeitbuchungstypRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        zeitbuchungstypRepository.deleteById(id);
    }

    @Override
    public List<Zeitbuchungstyp> findAllByIdentifier(String identifier) {
        return List.of();
    }

    @Override
    public Zeitbuchungstyp findByType(String type) {
        return zeitbuchungstypRepository.findFirstByTypeIgnoreCase(type).orElse(null);
    }
}

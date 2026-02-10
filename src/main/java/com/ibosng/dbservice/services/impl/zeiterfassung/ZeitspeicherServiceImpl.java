package com.ibosng.dbservice.services.impl.zeiterfassung;

import com.ibosng.dbservice.entities.zeiterfassung.Zeitspeicher;
import com.ibosng.dbservice.repositories.zeiterfassung.ZeitspeicherRepository;
import com.ibosng.dbservice.services.zeiterfassung.ZeitspeicherService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ZeitspeicherServiceImpl implements ZeitspeicherService {

    private final ZeitspeicherRepository zeitspeicherRepository;

    public ZeitspeicherServiceImpl(ZeitspeicherRepository zeitspeicherRepository) {
        this.zeitspeicherRepository = zeitspeicherRepository;
    }

    @Override
    public List<Zeitspeicher> findAll() {
        return zeitspeicherRepository.findAll();
    }

    @Override
    public Optional<Zeitspeicher> findById(Integer id) {
        return zeitspeicherRepository.findById(id);
    }

    @Override
    public Zeitspeicher save(Zeitspeicher zeitspeicher) {
        return zeitspeicherRepository.save(zeitspeicher);
    }

    @Override
    public List<Zeitspeicher> saveAll(List<Zeitspeicher> zeitspeicherList) {
        return zeitspeicherRepository.saveAll(zeitspeicherList);
    }

    @Override
    public void deleteById(Integer id) {
        zeitspeicherRepository.deleteById(id);
    }

    @Override
    public List<Zeitspeicher> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Zeitspeicher> findByAbbreviation(String abbreviation) {
        return zeitspeicherRepository.findByAbbreviation(abbreviation);
    }

    @Override
    public Optional<Zeitspeicher> findByZspNr(Integer zeitspeicherNummer) {
        return zeitspeicherRepository.findByZeitspeicherNummer(zeitspeicherNummer);
    }

    @Override
    public List<Zeitspeicher> findByZspNrIn(List<Integer> zspNummers) {
        return zeitspeicherRepository.findByZeitspeicherNummerIn(zspNummers);
    }
}

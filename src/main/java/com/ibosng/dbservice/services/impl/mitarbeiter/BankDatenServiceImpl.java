package com.ibosng.dbservice.services.impl.mitarbeiter;



import com.ibosng.dbservice.entities.mitarbeiter.BankDaten;
import com.ibosng.dbservice.repositories.mitarbeiter.BankDatenRepository;
import com.ibosng.dbservice.services.mitarbeiter.BankDatenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankDatenServiceImpl implements BankDatenService {

    private final BankDatenRepository bankDatenRepository;

    public BankDatenServiceImpl(BankDatenRepository bankDatenRepository) {
        this.bankDatenRepository = bankDatenRepository;
    }

    @Override
    public List<BankDaten> findAll() {
        return bankDatenRepository.findAll();
    }

    @Override
    public Optional<BankDaten> findById(Integer id) {
        return bankDatenRepository.findById(id);
    }

    @Override
    public BankDaten save(BankDaten object) {
        return bankDatenRepository.save(object);
    }

    @Override
    public List<BankDaten> saveAll(List<BankDaten> objects) {
        return bankDatenRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        bankDatenRepository.deleteById(id);
    }

    @Override
    public List<BankDaten> findAllByIdentifier(String identifier) {
        return null;
    }
}

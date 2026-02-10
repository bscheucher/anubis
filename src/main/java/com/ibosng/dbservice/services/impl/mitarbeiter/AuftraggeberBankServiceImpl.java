package com.ibosng.dbservice.services.impl.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.AuftraggeberBank;
import com.ibosng.dbservice.repositories.mitarbeiter.AuftraggeberBankRepository;
import com.ibosng.dbservice.services.mitarbeiter.AuftraggeberBankService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuftraggeberBankServiceImpl implements AuftraggeberBankService {

    private final AuftraggeberBankRepository auftraggeberBankRepository;

    public AuftraggeberBankServiceImpl(AuftraggeberBankRepository auftraggeberBankRepository) {
        this.auftraggeberBankRepository = auftraggeberBankRepository;
    }

    @Override
    public List<AuftraggeberBank> findAll() {
        return auftraggeberBankRepository.findAll();
    }

    @Override
    public Optional<AuftraggeberBank> findById(Integer id) {
        return auftraggeberBankRepository.findById(id);
    }

    @Override
    public AuftraggeberBank save(AuftraggeberBank object) {
        return auftraggeberBankRepository.save(object);
    }

    @Override
    public List<AuftraggeberBank> saveAll(List<AuftraggeberBank> objects) {
        return auftraggeberBankRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        auftraggeberBankRepository.deleteById(id);
    }

    @Override
    public List<AuftraggeberBank> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Optional<AuftraggeberBank> findByFirmenbankName(String firmenbankName) {
        return auftraggeberBankRepository.findByFirmenbankName(firmenbankName);
    }
}

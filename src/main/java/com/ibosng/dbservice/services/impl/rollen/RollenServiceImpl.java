package com.ibosng.dbservice.services.impl.rollen;

import com.ibosng.dbservice.entities.rollen.Rollen;
import com.ibosng.dbservice.repositories.rollen.RollenRepository;
import com.ibosng.dbservice.services.rollen.RollenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RollenServiceImpl implements RollenService {

    private final RollenRepository rollenRepository;

    public RollenServiceImpl(RollenRepository rollenRepository) {
        this.rollenRepository = rollenRepository;
    }

    @Override
    public Optional<Rollen> findById(Integer id) {
        return rollenRepository.findById(id);
    }

    @Override
    public List<Rollen> findAll() {
        return rollenRepository.findAll();
    }

    @Override
    public Rollen save(Rollen rollen) {
        return rollenRepository.save(rollen);
    }

    @Override
    public void deleteById(Integer id) {
        this.rollenRepository.deleteById(id);
    }

    @Override
    public List<Rollen> saveAll(List<Rollen> rollens) {
        return rollenRepository.saveAll(rollens);
    }

    @Override
    public List<Rollen> findAllByIdentifier(String identifier) {
        return null;
    }
}

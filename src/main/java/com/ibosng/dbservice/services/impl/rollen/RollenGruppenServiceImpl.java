package com.ibosng.dbservice.services.impl.rollen;

import com.ibosng.dbservice.entities.rollen.RollenGruppen;
import com.ibosng.dbservice.repositories.rollen.RollenGruppenRepository;
import com.ibosng.dbservice.services.rollen.RollenGruppenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RollenGruppenServiceImpl implements RollenGruppenService {

    private final RollenGruppenRepository rollenGruppenRepository;

    public RollenGruppenServiceImpl(RollenGruppenRepository rollenGruppenRepository) {
        this.rollenGruppenRepository = rollenGruppenRepository;
    }

    @Override
    public Optional<RollenGruppen> findById(Integer id) {
        return rollenGruppenRepository.findById(id);
    }

    @Override
    public List<RollenGruppen> findAll() {
        return rollenGruppenRepository.findAll();
    }

    @Override
    public RollenGruppen save(RollenGruppen rollenGruppen) {
        return rollenGruppenRepository.save(rollenGruppen);
    }

    @Override
    public void deleteById(Integer id) {
        this.rollenGruppenRepository.deleteById(id);
    }

    @Override
    public List<RollenGruppen> saveAll(List<RollenGruppen> rollenGruppens) {
        return rollenGruppenRepository.saveAll(rollenGruppens);
    }

    @Override
    public List<RollenGruppen> findAllByIdentifier(String identifier) {
        return null;
    }
}

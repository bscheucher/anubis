package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.masterdata.IbisFirma2Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.repositories.masterdata.IbisFirma2KostenstelleRepository;
import com.ibosng.dbservice.services.masterdata.IbisFirma2KostenstelleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class IbisFirma2KostenstelleServiceImpl implements IbisFirma2KostenstelleService {

    private final IbisFirma2KostenstelleRepository ibisFirma2KostenstelleRepository;

    public IbisFirma2KostenstelleServiceImpl(IbisFirma2KostenstelleRepository ibisFirma2KostenstelleRepository) {
        this.ibisFirma2KostenstelleRepository = ibisFirma2KostenstelleRepository;
    }

    @Override
    public List<IbisFirma2Kostenstelle> findAll() {
        return ibisFirma2KostenstelleRepository.findAll();
    }

    @Override
    public Optional<IbisFirma2Kostenstelle> findById(Integer id) {
        return ibisFirma2KostenstelleRepository.findById(id);
    }

    @Override
    public IbisFirma2Kostenstelle save(IbisFirma2Kostenstelle object) {
        return ibisFirma2KostenstelleRepository.save(object);
    }

    @Override
    public List<IbisFirma2Kostenstelle> saveAll(List<IbisFirma2Kostenstelle> objects) {
        return ibisFirma2KostenstelleRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        ibisFirma2KostenstelleRepository.deleteById(id);
    }

    @Override
    public List<IbisFirma2Kostenstelle> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public List<Kostenstelle> findKostenstelleByIbisFirmaId(Integer ibisFirmaId) {
        return ibisFirma2KostenstelleRepository.findKostenstelleByIbisFirmaId(ibisFirmaId);
    }

    @Override
    public List<Kostenstelle> findKostenstelleByIbisFirmaName(String ibisFirmaName) {
        return ibisFirma2KostenstelleRepository.findKostenstelleByIbisFirmaName(ibisFirmaName);
    }

    @Override
    public Kostenstelle findKostenstelleByIbisFirmaNameAndKostenstelleBezeichnung(String ibisFirmaName, String kostenstelleBezeichnung) {
        List<Kostenstelle> kostenstellen = ibisFirma2KostenstelleRepository.findKostenstelleByIbisFirmaNameAndKostenstelleBezeichnung(ibisFirmaName, kostenstelleBezeichnung);
        return findFirstObject(kostenstellen, new HashSet<>(Set.of(ibisFirmaName, kostenstelleBezeichnung)), "Kostenstelle");
    }

}

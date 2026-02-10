package com.ibosng.dbservice.services.impl.masterdata;


import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Gehalt;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.repositories.masterdata.GehaltRepository;
import com.ibosng.dbservice.services.masterdata.GehaltService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.ibosng.dbservice.utils.Helpers.findFirstObject;

@Service
public class GehaltServiceImpl implements GehaltService {

    private final GehaltRepository gehaltRepository;

    public GehaltServiceImpl(GehaltRepository gehaltRepository) {
        this.gehaltRepository = gehaltRepository;
    }

    @Override
    public List<Gehalt> findAll() {
        return gehaltRepository.findAll();
    }

    @Override
    public Optional<Gehalt> findById(Integer id) {
        return gehaltRepository.findById(id);
    }

    @Override
    public Gehalt save(Gehalt object) {
        return gehaltRepository.save(object);
    }

    @Override
    public List<Gehalt> saveAll(List<Gehalt> objects) {
        return gehaltRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        gehaltRepository.deleteById(id);
    }

    @Override
    public List<Gehalt> findAllByIdentifier(String identifier) {
        return null;
    }

    @Override
    public Gehalt findAllByKvStufeAndVerwendungsgruppe(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe) {
        Set<String> identifiers = new HashSet<>();
        identifiers.add(kvStufe.getName());
        identifiers.add(verwendungsgruppe.getName());
        return findFirstObject(gehaltRepository.findAllByKvStufeAndVerwendungsgruppe(kvStufe, verwendungsgruppe), identifiers, "Gehalt");
    }

    @Override
    public Gehalt findAllByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe, Status status) {
        Set<String> identifiers = new HashSet<>();
        identifiers.add(kvStufe.getName());
        identifiers.add(verwendungsgruppe.getName());
        return findFirstObject(gehaltRepository.findAllByKvStufeAndVerwendungsgruppeAndStatus(kvStufe, verwendungsgruppe, status), identifiers, "Gehalt");

    }

    @Override
    public Gehalt findCurrentByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe, Status status, LocalDate date) {
        return gehaltRepository.findCurrentByKvStufeAndVerwendungsgruppeAndStatus(kvStufe, verwendungsgruppe, status, date);
    }
}

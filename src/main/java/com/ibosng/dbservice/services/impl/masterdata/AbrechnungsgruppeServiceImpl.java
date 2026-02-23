package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.masterdata.Abrechnungsgruppe;
import com.ibosng.dbservice.repositories.masterdata.AbrechnungsgruppeRepository;
import com.ibosng.dbservice.services.masterdata.AbrechnungsgruppeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbrechnungsgruppeServiceImpl implements AbrechnungsgruppeService {

    private final AbrechnungsgruppeRepository abrechnungsgruppeRepository;

    public AbrechnungsgruppeServiceImpl(AbrechnungsgruppeRepository abrechnungsgruppeRepository) {
        this.abrechnungsgruppeRepository = abrechnungsgruppeRepository;
    }

    @Override
    public List<Abrechnungsgruppe> findAll() {
        return abrechnungsgruppeRepository.findAll();
    }

    @Override
    public Optional<Abrechnungsgruppe> findById(Integer id) {
        return abrechnungsgruppeRepository.findById(id);
    }

    @Override
    public Abrechnungsgruppe save(Abrechnungsgruppe dienstnehmergruppe) {
        return abrechnungsgruppeRepository.save(dienstnehmergruppe);
    }

    @Override
    public List<Abrechnungsgruppe> saveAll(List<Abrechnungsgruppe> objects) {
        return abrechnungsgruppeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        abrechnungsgruppeRepository.deleteById(id);
    }

    @Override
    public List<Abrechnungsgruppe> findAllByIdentifier(String identifier) {
        return null;
    }

    public Optional<Abrechnungsgruppe> findByAbbreviation(String abbreviation) {
        return abrechnungsgruppeRepository.findByAbbreviation(abbreviation);
    }

    public List<Abrechnungsgruppe> findByBezeichnung(String bezeichnung) {
        return abrechnungsgruppeRepository.findByBezeichnung(bezeichnung);
    }

    @Override
    public List<Abrechnungsgruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung) {
        return abrechnungsgruppeRepository.findAllByAbbreviationAndBezeichnung(abbreviation, bezeichnung);
    }
}

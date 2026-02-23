package com.ibosng.dbservice.services.impl.masterdata;

import com.ibosng.dbservice.entities.masterdata.Dienstnehmergruppe;
import com.ibosng.dbservice.repositories.masterdata.DienstnehmergruppeRepository;
import com.ibosng.dbservice.services.masterdata.DienstnehmergruppeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DienstnehmergruppeServiceImpl implements DienstnehmergruppeService {

    private final DienstnehmergruppeRepository dienstnehmergruppeRepository;

    public DienstnehmergruppeServiceImpl(DienstnehmergruppeRepository dienstnehmergruppeRepository) {
        this.dienstnehmergruppeRepository = dienstnehmergruppeRepository;
    }

    @Override
    public List<Dienstnehmergruppe> findAll() {
        return dienstnehmergruppeRepository.findAll();
    }

    @Override
    public Optional<Dienstnehmergruppe> findById(Integer id) {
        return dienstnehmergruppeRepository.findById(id);
    }

    @Override
    public Dienstnehmergruppe save(Dienstnehmergruppe dienstnehmergruppe) {
        return dienstnehmergruppeRepository.save(dienstnehmergruppe);
    }

    @Override
    public List<Dienstnehmergruppe> saveAll(List<Dienstnehmergruppe> objects) {
        return dienstnehmergruppeRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        dienstnehmergruppeRepository.deleteById(id);
    }

    @Override
    public List<Dienstnehmergruppe> findAllByIdentifier(String identifier) {
        return null;
    }

    public Optional<Dienstnehmergruppe> findByAbbreviation(String abbreviation){
        return dienstnehmergruppeRepository.findByAbbreviation(abbreviation);
    }

    public List<Dienstnehmergruppe> findByBezeichnung(String bezeichnung){
        return dienstnehmergruppeRepository.findByBezeichnung(bezeichnung);
    }

    @Override
    public List<Dienstnehmergruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung) {
        return dienstnehmergruppeRepository.findAllByAbbreviationAndBezeichnung(abbreviation, bezeichnung);
    }
}

package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.PersonalbogenIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.PersonalbogenIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.PersonalbogenIbosService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PersonalbogenIbosServiceImpl implements PersonalbogenIbosService {

    private final PersonalbogenIbosRepository personalbogenIbosRepository;

    public PersonalbogenIbosServiceImpl(PersonalbogenIbosRepository personalbogenIbosRepository) {
        this.personalbogenIbosRepository = personalbogenIbosRepository;
    }

    @Override
    public List<PersonalbogenIbos> findAll() {
        return personalbogenIbosRepository.findAll();
    }

    @Override
    public Optional<PersonalbogenIbos> findById(Integer id) {
        return personalbogenIbosRepository.findById(id);
    }

    @Override
    public PersonalbogenIbos save(PersonalbogenIbos object) {
        return personalbogenIbosRepository.save(object);
    }

    @Override
    public List<PersonalbogenIbos> saveAll(List<PersonalbogenIbos> objects) {
        return personalbogenIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        personalbogenIbosRepository.deleteById(id);
    }

    @Override
    public List<PersonalbogenIbos> findAllByAdresseAdnr(Integer adresseAdnr) {
        return personalbogenIbosRepository.findAllByAdresseAdnr(adresseAdnr);
    }

    @Override
    public List<PersonalbogenIbos> findAllByChangedAfterAndAderuser(String createdBy, LocalDateTime after) {
        return personalbogenIbosRepository.findAllByPbEruserAndPbErdaAfterOrPbEruserAndPbAedaAfter(createdBy, after, createdBy, after);
    }
}

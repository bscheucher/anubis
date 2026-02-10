package com.ibosng.dbibosservice.services.impl.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbos;
import com.ibosng.dbibosservice.repositories.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbosRepository;
import com.ibosng.dbibosservice.services.mitarbeiter.ArbeitsvertragZusatzFixKatCheckedIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArbeitsvertragZusatzFixKatCheckedIbosServiceImpl implements ArbeitsvertragZusatzFixKatCheckedIbosService {

    private final ArbeitsvertragZusatzFixKatCheckedIbosRepository arbeitsvertragZusatzFixKatCheckedIbosRepository;

    public ArbeitsvertragZusatzFixKatCheckedIbosServiceImpl(ArbeitsvertragZusatzFixKatCheckedIbosRepository arbeitsvertragZusatzFixKatCheckedIbosRepository) {
        this.arbeitsvertragZusatzFixKatCheckedIbosRepository = arbeitsvertragZusatzFixKatCheckedIbosRepository;
    }

    @Override
    public List<ArbeitsvertragZusatzFixKatCheckedIbos> findAll() {
        return arbeitsvertragZusatzFixKatCheckedIbosRepository.findAll();
    }

    @Override
    public Optional<ArbeitsvertragZusatzFixKatCheckedIbos> findById(Integer id) {
        return arbeitsvertragZusatzFixKatCheckedIbosRepository.findById(id);
    }

    @Override
    public ArbeitsvertragZusatzFixKatCheckedIbos save(ArbeitsvertragZusatzFixKatCheckedIbos object) {
        return arbeitsvertragZusatzFixKatCheckedIbosRepository.save(object);
    }

    @Override
    public List<ArbeitsvertragZusatzFixKatCheckedIbos> saveAll(List<ArbeitsvertragZusatzFixKatCheckedIbos> objects) {
        return arbeitsvertragZusatzFixKatCheckedIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        arbeitsvertragZusatzFixKatCheckedIbosRepository.deleteById(id);
    }

    @Override
    public List<ArbeitsvertragZusatzFixKatCheckedIbos> findAllByIdArbeitsvertragZusatzId(Integer arbeitsvertragZusatzId) {
        return arbeitsvertragZusatzFixKatCheckedIbosRepository.findAllByIdArbeitsvertragZusatzId(arbeitsvertragZusatzId);
    }
}

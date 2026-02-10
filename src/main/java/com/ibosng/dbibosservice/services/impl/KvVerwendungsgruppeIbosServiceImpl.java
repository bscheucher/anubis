package com.ibosng.dbibosservice.services.impl;

import com.ibosng.dbibosservice.entities.KvVerwendungsgruppeIbos;
import com.ibosng.dbibosservice.repositories.KvVerwendungsgruppeIbosRepository;
import com.ibosng.dbibosservice.services.KvVerwendungsgruppeIbosService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KvVerwendungsgruppeIbosServiceImpl implements KvVerwendungsgruppeIbosService {

    private final KvVerwendungsgruppeIbosRepository kvVerwendungsgruppeIbosRepository;

    public KvVerwendungsgruppeIbosServiceImpl(KvVerwendungsgruppeIbosRepository kvVerwendungsgruppeIbosRepository) {
        this.kvVerwendungsgruppeIbosRepository = kvVerwendungsgruppeIbosRepository;
    }

    @Override
    public List<KvVerwendungsgruppeIbos> findAll() {
        return kvVerwendungsgruppeIbosRepository.findAll();
    }

    @Override
    public Optional<KvVerwendungsgruppeIbos> findById(Integer id) {
        return kvVerwendungsgruppeIbosRepository.findById(id);
    }

    @Override
    public KvVerwendungsgruppeIbos save(KvVerwendungsgruppeIbos object) {
        return kvVerwendungsgruppeIbosRepository.save(object);
    }

    @Override
    public List<KvVerwendungsgruppeIbos> saveAll(List<KvVerwendungsgruppeIbos> objects) {
        return kvVerwendungsgruppeIbosRepository.saveAll(objects);
    }

    @Override
    public void deleteById(Integer id) {
        kvVerwendungsgruppeIbosRepository.deleteById(id);
    }

    @Override
    public List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnung(Integer kollektivvertragId, String bezeichnung) {
        return kvVerwendungsgruppeIbosRepository.findAllByKollektivvertragIdAndBezeichnung(kollektivvertragId, bezeichnung);
    }

    @Override
    public List<KvVerwendungsgruppeIbos> findAllByKollektivvertragIdAndBezeichnungAndBmdId(Integer kollektivvertragId, String bezeichnung, Integer bmdId) {
        return kvVerwendungsgruppeIbosRepository.findAllByKollektivvertragIdAndBezeichnungAndBmdId(kollektivvertragId, bezeichnung, bmdId);
    }
}
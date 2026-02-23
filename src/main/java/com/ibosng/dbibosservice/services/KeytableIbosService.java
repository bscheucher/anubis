package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.KeytableIbos;

import java.util.List;
import java.util.Optional;

public interface KeytableIbosService extends BaseService<KeytableIbos> {
    List<KeytableIbos> findAllByKyNameAndKyValueT1(String kyName, String kyValuet1);

    List<KeytableIbos> findAllByKyNameAndKyNrOrderByKyIndex(String kyName, int kyNr);

    KeytableIbos findFirstByKyNameAndKyNrOrderByKyIndex(String kyName, int kyNr);

    Optional<KeytableIbos> findByKyNr(Integer kyNr);
}

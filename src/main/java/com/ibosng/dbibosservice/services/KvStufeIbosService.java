package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.KvStufeIbos;

import java.util.List;

public interface KvStufeIbosService extends BaseService<KvStufeIbos> {
    List<KvStufeIbos> findAllByBezeichnungAndKvVerwendungsgruppeId(String bezeichnung, Integer verwendungsgruppe);
}

package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.FKostenstelleIbos;

import java.util.List;

public interface FKostenstelleIbosService extends BaseService<FKostenstelleIbos> {
    List<FKostenstelleIbos> findAllByIdKstKstGr(Integer kstgr);

    List<FKostenstelleIbos> findAllByIdKstKstNr(Integer kstnr);
}

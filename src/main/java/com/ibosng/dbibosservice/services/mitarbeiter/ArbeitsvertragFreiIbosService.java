package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragFreiIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface ArbeitsvertragFreiIbosService extends BaseService<ArbeitsvertragFreiIbos> {
    List<ArbeitsvertragFreiIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);

}

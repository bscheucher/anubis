package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFreiIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface ArbeitsvertragZusatzFreiIbosService extends BaseService<ArbeitsvertragZusatzFreiIbos> {
    List<ArbeitsvertragZusatzFreiIbos> findAllByArbeitsvertragZusatzId(Integer arbeitstvertragzusatzId);

}

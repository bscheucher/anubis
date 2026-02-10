package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragZusatzFixIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface ArbeitsvertragZusatzFixIbosService extends BaseService<ArbeitsvertragZusatzFixIbos> {
    List<ArbeitsvertragZusatzFixIbos> findAllByArbeitsvertragZusatzId(Integer arbeitstvertragzusatzId);

}

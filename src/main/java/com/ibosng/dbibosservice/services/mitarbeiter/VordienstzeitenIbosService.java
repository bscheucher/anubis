package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.VordienstzeitenIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface VordienstzeitenIbosService extends BaseService<VordienstzeitenIbos> {
    List<VordienstzeitenIbos> findAllByArbeitsvertragId(Integer arbeitsvertragId);
    List<VordienstzeitenIbos> findAllByPersonalbogenId(Integer personalbogenId);

}

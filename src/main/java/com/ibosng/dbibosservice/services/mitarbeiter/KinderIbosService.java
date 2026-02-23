package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.KinderIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface KinderIbosService extends BaseService<KinderIbos> {
    List<KinderIbos> findAllByAdresseAdnr(Integer adresseAdnr);

}

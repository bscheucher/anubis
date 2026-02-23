package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.GrAdIbos;

public interface GrAdIbosService extends BaseService<GrAdIbos> {
    GrAdIbos findAllByAdresseAdadnr(Integer adresseAdadnr);

}

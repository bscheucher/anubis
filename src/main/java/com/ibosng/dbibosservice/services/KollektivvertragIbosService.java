package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.KollektivvertragIbos;

public interface KollektivvertragIbosService extends BaseService<KollektivvertragIbos> {
    KollektivvertragIbos findByBezeichnung(String bezeichnung);
}

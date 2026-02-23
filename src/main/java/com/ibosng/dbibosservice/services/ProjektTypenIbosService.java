package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.ProjektTypenIbos;

public interface ProjektTypenIbosService extends BaseService<ProjektTypenIbos> {
    ProjektTypenIbos findByBezeichnung(String bezeichnung);
}

package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.MutterspracheIbos;

public interface MutterspracheIbosService extends BaseService<MutterspracheIbos> {
    MutterspracheIbos findByName(String name);
}

package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.StaatenIbos;

import java.util.List;

public interface StaatenIbosService extends BaseService<StaatenIbos> {
    List<StaatenIbos> findAllByAlpha2AndAlpha3(String alpha2, String alpha3);
}

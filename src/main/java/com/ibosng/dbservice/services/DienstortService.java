package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Dienstort;

import java.util.List;

public interface DienstortService extends BaseService<Dienstort>{

    List<Dienstort> findAllByName(String name);
}

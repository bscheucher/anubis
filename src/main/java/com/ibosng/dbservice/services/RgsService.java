package com.ibosng.dbservice.services;

import com.ibosng.dbservice.entities.Rgs;

import java.util.List;

public interface RgsService extends BaseService<Rgs> {
    List<Integer> getAllRgs();

    Rgs findByRgs(Integer rgs);
}

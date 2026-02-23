package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.ProjektPk;

import java.util.List;

public interface ProjektPkService extends BaseService<ProjektPk> {
    List<ProjektPk> findByProjectId(Integer projektId);
}

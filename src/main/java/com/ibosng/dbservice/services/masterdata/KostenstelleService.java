package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface KostenstelleService extends BaseService<Kostenstelle> {
    Kostenstelle findByBezeichnung(String name);

    Kostenstelle findByNummer(Integer id);

    List<Kostenstelle> findAllByStatus(Status status);
}

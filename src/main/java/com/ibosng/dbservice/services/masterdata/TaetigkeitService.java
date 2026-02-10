package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Taetigkeit;
import com.ibosng.dbservice.services.BaseService;

public interface TaetigkeitService extends BaseService<Taetigkeit> {

    Taetigkeit findByName(String name);
}

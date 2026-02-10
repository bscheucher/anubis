package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Anrede;
import com.ibosng.dbservice.services.BaseService;

public interface AnredeService extends BaseService<Anrede> {

    Anrede findByName(String name);
}

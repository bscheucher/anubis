package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Titel;
import com.ibosng.dbservice.services.BaseService;

public interface TitelService extends BaseService<Titel> {

    Titel findByName(String name);
}

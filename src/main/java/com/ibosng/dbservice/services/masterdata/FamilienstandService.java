package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Familienstand;
import com.ibosng.dbservice.services.BaseService;

public interface FamilienstandService extends BaseService<Familienstand> {

    Familienstand findByName(String name);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Klasse;
import com.ibosng.dbservice.services.BaseService;

public interface KlasseService extends BaseService<Klasse> {

    Klasse findByName(String name);
}

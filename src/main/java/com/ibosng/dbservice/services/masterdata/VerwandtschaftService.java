package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Verwandtschaft;
import com.ibosng.dbservice.services.BaseService;

public interface VerwandtschaftService extends BaseService<Verwandtschaft> {

    Verwandtschaft findByName(String name);
}

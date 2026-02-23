package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Arbeitsgenehmigung;
import com.ibosng.dbservice.services.BaseService;

public interface ArbeitsgenehmigungService extends BaseService<Arbeitsgenehmigung> {

    Arbeitsgenehmigung findByName(String name);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Arbeitszeitmodell;
import com.ibosng.dbservice.services.BaseService;

public interface ArbeitszeitmodellService extends BaseService<Arbeitszeitmodell> {

    Arbeitszeitmodell findByName(String name);
}

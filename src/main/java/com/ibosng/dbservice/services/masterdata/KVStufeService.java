package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.services.BaseService;

public interface KVStufeService extends BaseService<KVStufe> {

    KVStufe findByTotalMonths(int totalMonths);

    KVStufe findAllByName(String name);
}

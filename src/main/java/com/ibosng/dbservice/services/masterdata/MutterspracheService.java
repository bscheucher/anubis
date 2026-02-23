package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Muttersprache;
import com.ibosng.dbservice.services.BaseService;

public interface MutterspracheService extends BaseService<Muttersprache> {

    Muttersprache findByName(String language);
}
package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsausmass;
import com.ibosng.dbservice.services.BaseService;

public interface BeschaeftigungsausmassService extends BaseService<Beschaeftigungsausmass> {

    Beschaeftigungsausmass findByName(String name);
}

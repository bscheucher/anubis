package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.teilnehmer.Beruf;
import com.ibosng.dbservice.services.BaseService;

public interface BerufService extends BaseService<Beruf> {

    Beruf findByName(String name);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Vertragsart;
import com.ibosng.dbservice.services.BaseService;

public interface VertragsartService extends BaseService<Vertragsart> {

    Vertragsart findByName(String name);
}

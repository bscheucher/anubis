package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.AbgeltungUeberstunden;
import com.ibosng.dbservice.services.BaseService;

public interface AbgeltungUeberstundenService extends BaseService<AbgeltungUeberstunden> {

    AbgeltungUeberstunden findByName(String name);
}

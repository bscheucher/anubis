package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.services.BaseService;

public interface GeschlechtService extends BaseService<Geschlecht> {

    Geschlecht findByName(String name);
    Geschlecht findByAbbreviation(String abbreviation);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Jobbeschreibung;
import com.ibosng.dbservice.services.BaseService;

public interface JobbeschreibungService extends BaseService<Jobbeschreibung> {

    Jobbeschreibung findByName(String name);
}

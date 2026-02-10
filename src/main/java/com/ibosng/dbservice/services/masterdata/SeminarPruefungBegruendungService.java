package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungBegruendung;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungBegruendungService extends BaseService<SeminarPruefungBegruendung> {

    SeminarPruefungBegruendung findByName(String name);
}

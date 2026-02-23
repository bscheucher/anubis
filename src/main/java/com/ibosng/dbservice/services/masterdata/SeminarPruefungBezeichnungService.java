package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungBezeichnung;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungBezeichnungService extends BaseService<SeminarPruefungBezeichnung> {

    SeminarPruefungBezeichnung findByName(String name);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungGegenstand;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungGegenstandService extends BaseService<SeminarPruefungGegenstand> {

    SeminarPruefungGegenstand findByName(String name);
}

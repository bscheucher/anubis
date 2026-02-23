package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungErgebnisType;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungErgebnisTypeService extends BaseService<SeminarPruefungErgebnisType> {

    SeminarPruefungErgebnisType findByName(String name);
}

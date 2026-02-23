package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungInstitut;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungInstitutService extends BaseService<SeminarPruefungInstitut> {

    SeminarPruefungInstitut findByName(String name);
}

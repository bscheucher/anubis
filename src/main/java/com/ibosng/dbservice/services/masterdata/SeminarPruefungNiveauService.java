package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarPruefungNiveau;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarPruefungNiveauService extends BaseService<SeminarPruefungNiveau> {

    SeminarPruefungNiveau findByName(String name);
}

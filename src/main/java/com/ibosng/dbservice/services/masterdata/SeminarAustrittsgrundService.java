package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.seminar.SeminarAustrittsgrund;
import com.ibosng.dbservice.services.BaseService;

public interface SeminarAustrittsgrundService extends BaseService<SeminarAustrittsgrund> {

    SeminarAustrittsgrund findByName(String name);
}

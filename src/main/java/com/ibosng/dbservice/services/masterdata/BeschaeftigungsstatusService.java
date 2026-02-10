package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Beschaeftigungsstatus;
import com.ibosng.dbservice.services.BaseService;

public interface BeschaeftigungsstatusService extends BaseService<Beschaeftigungsstatus> {

    Beschaeftigungsstatus findByName(String name);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.SprachkenntnisNiveau;
import com.ibosng.dbservice.services.BaseService;

public interface SprachkenntnisNiveauService extends BaseService<SprachkenntnisNiveau> {

    SprachkenntnisNiveau findByName(String name);
}
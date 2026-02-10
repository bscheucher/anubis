package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Kategorie;
import com.ibosng.dbservice.services.BaseService;

public interface KategorieService extends BaseService<Kategorie> {

    Kategorie findByName(String name);
}

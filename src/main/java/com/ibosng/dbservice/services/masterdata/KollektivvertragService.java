package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Kollektivvertrag;
import com.ibosng.dbservice.services.BaseService;

public interface KollektivvertragService extends BaseService<Kollektivvertrag> {

    Kollektivvertrag findByName(String name);
}

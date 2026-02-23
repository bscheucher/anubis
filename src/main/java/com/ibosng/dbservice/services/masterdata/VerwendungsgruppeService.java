package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface VerwendungsgruppeService extends BaseService<Verwendungsgruppe> {
    Verwendungsgruppe findByName(String name);
    List<Verwendungsgruppe> findAllByKollektivvertragName(String kollektivvertrag);
}

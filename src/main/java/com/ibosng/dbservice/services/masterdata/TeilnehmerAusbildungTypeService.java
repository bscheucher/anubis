package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.teilnehmer.TnAusbildungType;
import com.ibosng.dbservice.services.BaseService;

public interface TeilnehmerAusbildungTypeService extends BaseService<TnAusbildungType> {

    TnAusbildungType findByName(String name);
}

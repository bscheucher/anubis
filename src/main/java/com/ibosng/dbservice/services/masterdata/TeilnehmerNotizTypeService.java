package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizType;
import com.ibosng.dbservice.services.BaseService;

public interface TeilnehmerNotizTypeService extends BaseService<TeilnehmerNotizType> {

    TeilnehmerNotizType findByName(String name);
}

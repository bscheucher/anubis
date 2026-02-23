package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.Arbeitszeiten;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface ArbeitszeitenService extends BaseService<Arbeitszeiten> {
    List<Arbeitszeiten> findByArbAndArbeitszeitenInfoId(Integer arbeitszeiteninfoId);

}

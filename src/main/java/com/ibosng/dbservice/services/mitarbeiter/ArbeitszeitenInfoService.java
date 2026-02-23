package com.ibosng.dbservice.services.mitarbeiter;

import com.ibosng.dbservice.entities.mitarbeiter.ArbeitszeitenInfo;
import com.ibosng.dbservice.entities.mitarbeiter.Vertragsdaten;
import com.ibosng.dbservice.services.BaseService;

public interface ArbeitszeitenInfoService extends BaseService<ArbeitszeitenInfo> {
    ArbeitszeitenInfo findByVertragsdatenId(Integer vertragsdatenId);

    ArbeitszeitenInfo createNewArbeitszeitInfo(Vertragsdaten vertragsdaten, String createdBy);
}

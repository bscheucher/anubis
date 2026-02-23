package com.ibosng.dbservice.services.lhr;

import com.ibosng.dbservice.entities.lhr.Erreichbarkeit;
import com.ibosng.dbservice.services.BaseService;

public interface ErreichbarkeitService extends BaseService<Erreichbarkeit> {
    Erreichbarkeit findByErreichbarkeitsart(String erreichbarkeitsart);
    Erreichbarkeit findByKz(String kz);
}

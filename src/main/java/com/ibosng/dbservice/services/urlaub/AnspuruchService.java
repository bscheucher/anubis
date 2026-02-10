package com.ibosng.dbservice.services.urlaub;

import com.ibosng.dbservice.entities.urlaub.Anspruch;
import com.ibosng.dbservice.services.BaseService;

public interface AnspuruchService extends BaseService<Anspruch> {
    Anspruch findByLhrId(Integer lhrId);

    Anspruch findByBezeichnung(String bezeichnung);
}

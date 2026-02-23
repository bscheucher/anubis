package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface IbisFirmaService extends BaseService<IbisFirma> {
    IbisFirma findByName(String name);

    IbisFirma findByShortName(String shortName);

    List<IbisFirma> findAllByStatus(Status status);

    IbisFirma findByBmdClient(Integer bmdClient);
}

package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.IbisFirma2Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;

public interface IbisFirma2KostenstelleService extends BaseService<IbisFirma2Kostenstelle> {
    List<Kostenstelle> findKostenstelleByIbisFirmaId(Integer ibisFirmaId);
    List<Kostenstelle> findKostenstelleByIbisFirmaName(String ibisFirmaName);
    Kostenstelle findKostenstelleByIbisFirmaNameAndKostenstelleBezeichnung(String ibisFirmaName, String kostenstelleBezeichnung);

}

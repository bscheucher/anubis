package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.Gehalt;
import com.ibosng.dbservice.entities.masterdata.KVStufe;
import com.ibosng.dbservice.entities.masterdata.Verwendungsgruppe;
import com.ibosng.dbservice.services.BaseService;

import java.time.LocalDate;

public interface GehaltService extends BaseService<Gehalt> {

    Gehalt findAllByKvStufeAndVerwendungsgruppe(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe);
    Gehalt findAllByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe, Status status);
    Gehalt findCurrentByKvStufeAndVerwendungsgruppeAndStatus(KVStufe kvStufe, Verwendungsgruppe verwendungsgruppe, Status status, LocalDate date);
}

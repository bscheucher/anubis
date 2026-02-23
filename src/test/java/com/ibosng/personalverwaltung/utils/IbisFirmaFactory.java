package com.ibosng.personalverwaltung.utils;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;

public class IbisFirmaFactory {

    public static IbisFirma create() {
        var f = new IbisFirma();
        f.setStatus(Status.ACTIVE);
        f.setCreatedBy("somebody");
        f.setLhrNr(10);
        f.setLhrKz("abc");
        return f;
    }
}

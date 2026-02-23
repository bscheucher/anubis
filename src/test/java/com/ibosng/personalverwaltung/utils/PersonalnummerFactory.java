package com.ibosng.personalverwaltung.utils;

import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;

public class PersonalnummerFactory {

    public static Personalnummer createIn(IbisFirma firma) {
        var p = new Personalnummer();
        p.setPersonalnummer("123456789");
        p.setStatus(Status.ACTIVE);
        p.setCreatedBy("somebody");
        p.setFirma(firma);
        return p;
    }

    public static Personalnummer createWithFirma() {
        return createIn(IbisFirmaFactory.create());
    }
}

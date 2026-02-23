package com.ibosng.dbmapperservice.utils.mappers;

import com.ibosng.dbibosservice.entities.AusschreibungIbos;
import com.ibosng.dbservice.entities.Ausschreibung;

public class AusschreibungMapper {
    static AusschreibungIbos ausschreibungToAusschreibungIbos(Ausschreibung ausschreibung) {
        AusschreibungIbos ausschreibungIbos = new AusschreibungIbos();
        ausschreibungIbos.setAsnr(ausschreibung.getAusschreibungNummer());
        ausschreibungIbos.setAsbezeichnung1(ausschreibung.getBezeichnung());
        ausschreibungIbos.setAsbundesland(ausschreibung.getBundesland());
        ausschreibungIbos.setAserda(ausschreibung.getCreatedOn());
        ausschreibungIbos.setAseruser(ausschreibung.getCreatedBy());
        ausschreibungIbos.setAsaeda(ausschreibung.getChangedOn());
        ausschreibungIbos.setAsaeuser(ausschreibung.getChangedBy());
        return ausschreibungIbos;
    }

    static Ausschreibung ausschreibungIbosToAusschreibung(AusschreibungIbos ausschreibungIbos) {
        Ausschreibung ausschreibung = new Ausschreibung();
        ausschreibung.setAusschreibungNummer(ausschreibungIbos.getAsnr());
        ausschreibung.setBezeichnung(ausschreibungIbos.getAsbezeichnung1());
        ausschreibung.setBundesland(ausschreibungIbos.getAsbundesland());
        ausschreibung.setCreatedOn(ausschreibungIbos.getAserda());
        ausschreibung.setCreatedBy(ausschreibungIbos.getAseruser());
        ausschreibung.setChangedOn(ausschreibungIbos.getAsaeda());
        ausschreibung.setChangedBy(ausschreibungIbos.getAsaeuser());
        return ausschreibung;
    }
}

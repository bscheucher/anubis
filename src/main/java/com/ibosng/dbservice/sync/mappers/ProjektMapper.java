package com.ibosng.dbservice.sync.mappers;/*
package com.ibosng.dbservice.sync.mappers;

import com.ibosng.dbibosservice.entities.ProjektIbos;
import com.ibosng.dbservice.entities.Projekt;

public class ProjektMapper {

    public static Projekt projektIbosToProjekt(ProjektIbos projektIbos){
        Projekt projekt = new Projekt();
        projekt.setProjektNummer(projektIbos.getId());
        projekt.setAuftragNummer(projektIbos.getAsNr());
        projekt.setBezeichnung(projektIbos.getPjBezeichnung1());
        projekt.setStartDate(projektIbos.getPjDatumVon());
        projekt.setEndDate(projektIbos.getPjDatumBis());
        projekt.setKostenstelleGruppe(projektIbos.getPjKstGr());
        projekt.setKostenstelle(projektIbos.getPjKstNr());
        projekt.setKostenstelleSub(projektIbos.getPjKstSub());
        projekt.setKostentraeger(projektIbos.getPjKostentraeger());
        projekt.setCreatedOn(projektIbos.getPjErda());
        projekt.setCreatedBy(projektIbos.getPjEruser());
        projekt.setChangedOn(projektIbos.getPjAeda());
        projekt.setChangedBy(projektIbos.getPjAeuser());
        return projekt;
    }

    public static ProjektIbos projektToProjektIbos(Projekt projekt){
        ProjektIbos projektIbos = new ProjektIbos();
//        projektIbos.setPjNr(projekt.getProjektNummer());
        projektIbos.setAsNr(projekt.getAuftragNummer());
        projektIbos.setPjBezeichnung1(projekt.getBezeichnung());
        projektIbos.setPjDatumVon(projekt.getStartDate());
        projektIbos.setPjDatumBis(projekt.getEndDate());
        projektIbos.setPjKstGr(projekt.getKostenstelleGruppe());
        projektIbos.setPjKstNr(projekt.getKostenstelle());
        projektIbos.setPjKstSub(projekt.getKostenstelleSub());
        projektIbos.setPjKostentraeger(projekt.getKostentraeger());
        projektIbos.setPjAeda(projekt.getChangedOn());
        projektIbos.setPjAeuser(projekt.getChangedBy());
        projektIbos.setPjErda(projekt.getCreatedOn());
        projektIbos.setPjEruser(projekt.getCreatedBy());
        return projektIbos;
    }
}
*/

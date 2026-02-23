package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerNotizKategorie;
import com.ibosng.dbservice.services.BaseService;

public interface TeilnehmerNotizKategorieService extends BaseService<TeilnehmerNotizKategorie> {

    TeilnehmerNotizKategorie findByName(String name);
}

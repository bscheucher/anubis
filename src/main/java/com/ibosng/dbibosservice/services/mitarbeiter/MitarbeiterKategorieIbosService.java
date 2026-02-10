package com.ibosng.dbibosservice.services.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.MitarbeiterKategorieIbos;
import com.ibosng.dbibosservice.services.BaseService;

import java.util.List;

public interface MitarbeiterKategorieIbosService extends BaseService<MitarbeiterKategorieIbos> {
    List<MitarbeiterKategorieIbos> findAllByBezeichnung(String bezeichnung);

}

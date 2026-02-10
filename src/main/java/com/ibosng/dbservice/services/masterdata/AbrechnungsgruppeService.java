package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Abrechnungsgruppe;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface AbrechnungsgruppeService extends BaseService<Abrechnungsgruppe> {

    Optional<Abrechnungsgruppe> findByAbbreviation(String abbreviation);

    List<Abrechnungsgruppe> findByBezeichnung(String abbreviation);
    List<Abrechnungsgruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung);
}

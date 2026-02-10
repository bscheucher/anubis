package com.ibosng.dbservice.services.masterdata;

import com.ibosng.dbservice.entities.masterdata.Dienstnehmergruppe;
import com.ibosng.dbservice.services.BaseService;

import java.util.List;
import java.util.Optional;

public interface DienstnehmergruppeService extends BaseService<Dienstnehmergruppe> {

    Optional<Dienstnehmergruppe> findByAbbreviation(String abbreviation);

    List<Dienstnehmergruppe> findByBezeichnung(String abbreviation);
    List<Dienstnehmergruppe> findAllByAbbreviationAndBezeichnung(String abbreviation, String bezeichnung);
}

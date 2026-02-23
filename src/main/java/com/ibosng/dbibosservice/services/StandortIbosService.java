package com.ibosng.dbibosservice.services;

import com.ibosng.dbibosservice.entities.StandortIbos;

public interface StandortIbosService extends BaseService<StandortIbos> {
    Integer getLandIdFromLandCode(String landcode);
    Integer getRGS(String rgs);
    Integer getAnredeKeyValueFromAbbreviation(String geschlecht);
    Integer getAnredeKeyValueFromAbbreviationAndName(String geschlecht, String name);
    String getAnredeStringValue(Integer anredeId);
    String getLandIdFromId(Integer id);
    Integer getRGSNummer(Integer id);
    Integer getFamilienstandKey(String familienstand);
}

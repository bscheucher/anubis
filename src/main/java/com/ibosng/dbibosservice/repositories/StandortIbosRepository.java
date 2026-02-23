package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.StandortIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional("mariaDbTransactionManager")
public interface StandortIbosRepository extends JpaRepository<StandortIbos, Integer> {

    String GET_LAND_FROM_LAND_CODE = "select id from STAATEN where alpha2 = :landcode";

    String GET_LAND_FROM_ID = "select alpha2 from STAATEN where id = :id";

    String GET_RGS_KEY_VALUE = "select KYnr from KEYTABLE where KYname = 'RGS' and KYvaluenum1 = :rgs";

    String GET_RGS_NUMMER = "select KYvaluenum1 from KEYTABLE where KYname = 'RGS' and KYnr = :id";

    String GET_ANREDE_KEY_VALUE_FROM_ABBREVIATION = "select KYnr from KEYTABLE where KYname = 'Anrede' and KYvaluem1 = :geschlecht order by KYindex asc limit 1";

    String GET_ANREDE_KEY_VALUE_FROM_ABBREVIATION_AND_NAME = "select KYnr from KEYTABLE where KYname = 'Anrede' and KYvaluem1 = :geschlecht and KYvaluet1 = :name order by KYindex asc limit 1";

    String GET_ANREDE_STRING_VALUE = "select KYvaluet1 from KEYTABLE where KYname = 'Anrede' and KYnr = :anredeId order by KYindex asc limit 1";

    String GET_FAMILIENSTAND_KEY = "select KYnr from KEYTABLE where KYname = 'ADfamstand' and KYvaluet1 = :familienstand";

    @Query(value = GET_LAND_FROM_LAND_CODE, nativeQuery = true)
    Integer getLandIdFromLandCode(String landcode);
    @Query(value = GET_LAND_FROM_ID, nativeQuery = true)
    String getLandIdFromId(Integer id);

    @Query(value = GET_RGS_KEY_VALUE, nativeQuery = true)
    Integer getRGS(String rgs);

    @Query(value = GET_RGS_NUMMER, nativeQuery = true)
    Integer getRGSNummer(Integer id);

    @Query(value = GET_ANREDE_KEY_VALUE_FROM_ABBREVIATION, nativeQuery = true)
    Integer getAnredeKeyValueFromAbbreviation(String geschlecht);

    @Query(value = GET_ANREDE_KEY_VALUE_FROM_ABBREVIATION_AND_NAME, nativeQuery = true)
    Integer getAnredeKeyValueFromAbbreviationAndName(String geschlecht, String name);

    @Query(value = GET_ANREDE_STRING_VALUE, nativeQuery = true)
    String getAnredeStringValue(Integer anredeId);

    @Query(value = GET_FAMILIENSTAND_KEY, nativeQuery = true)
    Integer getFamilienstandKey(String familienstand);
}

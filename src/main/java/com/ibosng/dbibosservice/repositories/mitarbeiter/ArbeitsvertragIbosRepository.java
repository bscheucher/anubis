package com.ibosng.dbibosservice.repositories.mitarbeiter;

import com.ibosng.dbibosservice.entities.mitarbeiter.ArbeitsvertragIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface ArbeitsvertragIbosRepository extends JpaRepository<ArbeitsvertragIbos, Integer> {

    String CONTRACT_QUERY = """
            with v_arbeitsvertrag as (
                                       select
                                           MIN(ARBEITSVERTRAG_ZUSATZ.datum_von) AS earliest_datum_von,
                                           MAX(COALESCE(ARBEITSVERTRAG_ZUSATZ.datum_bis, '2900-01-01')) AS latest_datum_bis,
                                           kvgruppe.bezeichnung AS VERWENDUNGSGRUPPE,
                                           KV_STUFE.Bezeichnung as STUFE,
                                           kt.KYvaluet1 as FUNKTION,
                                           ARBEITSVERTRAG_ZUSATZ.geschaeftsbereich as KST,
                                           ARBEITSVERTRAG_ZUSATZ.persnr as PERSONAL_NUMMER,
             (ARBEITSVERTRAG_ZUSATZ_FIX.stdmo + ARBEITSVERTRAG_ZUSATZ_FIX.stddi + ARBEITSVERTRAG_ZUSATZ_FIX.stdmi + ARBEITSVERTRAG_ZUSATZ_FIX.stddo + ARBEITSVERTRAG_ZUSATZ_FIX.stdfr) AS wochenstunden,
                                           DPW_ARBEITSPLANMODELL.Bezeichnung as ARBEITSZEITMODEL,
                                           CONCAT(STANDORT.SOplz, ' ', STANDORT.SOort, ', ', STANDORT.SOstrasse) as DIENSTORT,
                                           NULL  as NICHTLEISTUNGEN
                                       from ARBEITSVERTRAG
                                       inner join ARBEITSVERTRAG_ZUSATZ on ARBEITSVERTRAG.id = ARBEITSVERTRAG_ZUSATZ.ARBEITSVERTRAG_id
                                       left join ARBEITSVERTRAG_ZUSATZ_FIX on ARBEITSVERTRAG_ZUSATZ.id = ARBEITSVERTRAG_ZUSATZ_FIX.ARBEITSVERTRAG_ZUSATZ_id
                                       left join ARBEITSVERTRAG_ZUSATZ_FREI on ARBEITSVERTRAG_ZUSATZ.id = ARBEITSVERTRAG_ZUSATZ_FREI.ARBEITSVERTRAG_ZUSATZ_id
                                       left join KV_STUFE on ARBEITSVERTRAG_ZUSATZ_FIX.stufe = KV_STUFE.id
                                       left join DPW_ARBEITSPLANMODELL on ARBEITSVERTRAG_ZUSATZ_FIX.Arbeitsplanmodell = DPW_ARBEITSPLANMODELL.id
                                       inner join STANDORT on STANDORT.SOstandortid = ARBEITSVERTRAG_ZUSATZ.dienstort
                                       join BENUTZER bn on bn.BNadnr = ARBEITSVERTRAG.ADRESSE_adnr
                                       join KV_VERWENDUNGSGRUPPE kvgruppe on kvgruppe.id = KV_STUFE.kv_verwendungsgruppe_id
                                       join KEYTABLE kt on kt.KYnr = ARBEITSVERTRAG_ZUSATZ.taetbezeichnung
                                       where (ARBEITSVERTRAG.geringfuegig_karenz <> '1' or ARBEITSVERTRAG.geringfuegig_karenz is null)
                                       and bn.BNadSAMAN like :user
                                       GROUP BY
                                           kvgruppe.bezeichnung,
                                           KV_STUFE.Bezeichnung,
                                           kt.KYvaluet1,
                                           ARBEITSVERTRAG_ZUSATZ.geschaeftsbereich,
                                           ARBEITSVERTRAG_ZUSATZ.persnr,
                                           DPW_ARBEITSPLANMODELL.Bezeichnung,
                                           STANDORT.SOort
                                       order by earliest_datum_von
                                       ) select v_arbeitsvertrag.earliest_datum_von as datum_von,
                                                CASE
                                                    WHEN v_arbeitsvertrag.latest_datum_bis = '2900-01-01' THEN NULL
                                                        ELSE v_arbeitsvertrag.latest_datum_bis
                                                    END
                                                    AS datum_bis,
                                                v_arbeitsvertrag.VERWENDUNGSGRUPPE,
                                                v_arbeitsvertrag.STUFE,
                                                v_arbeitsvertrag.FUNKTION,
                                                v_arbeitsvertrag.KST,
                                                v_arbeitsvertrag.PERSONAL_NUMMER,
                                                (select MIN(v_arbeitsvertrag.earliest_datum_von) from v_arbeitsvertrag) as EINTRITTSDATUM,
                                                v_arbeitsvertrag.wochenstunden,
                                                v_arbeitsvertrag.ARBEITSZEITMODEL,
                                                v_arbeitsvertrag.DIENSTORT,
                                                v_arbeitsvertrag.NICHTLEISTUNGEN
                                       from v_arbeitsvertrag""";

    String CONTRACT_WITHOUT_LEISTUNGEN_QUERY = "with v_arbeitsvertrag as ( " +
            "select distinct(an.datum_von) AS earliest_datum_von, " +
            "    an.datum_bis AS latest_datum_bis, " +
            "    null AS VERWENDUNGSGRUPPE, " +
            "    null as STUFE, " +
            "    null as FUNKTION, " +
            "    ARBEITSVERTRAG_ZUSATZ.geschaeftsbereich as KST, " +
            "    (ARBEITSVERTRAG_ZUSATZ_FIX.stdmo + ARBEITSVERTRAG_ZUSATZ_FIX.stddi + ARBEITSVERTRAG_ZUSATZ_FIX.stdmi + ARBEITSVERTRAG_ZUSATZ_FIX.stddo + ARBEITSVERTRAG_ZUSATZ_FIX.stdfr) AS wochenstunden, " +
            "    ARBEITSVERTRAG_ZUSATZ.persnr as PERSONAL_NUMMER, " +
            "    DPW_ARBEITSPLANMODELL.Bezeichnung as ARBEITSZEITMODEL, " +
            "    CONCAT(STANDORT.SOplz, ' ', STANDORT.SOort, ', ', STANDORT.SOstrasse) as DIENSTORT, " +
            "    an.art as NICHTLEISTUNGEN " +
            "from ARBEITSVERTRAG " +
            "inner join ARBEITSVERTRAG_ZUSATZ on ARBEITSVERTRAG.id = ARBEITSVERTRAG_ZUSATZ.ARBEITSVERTRAG_id " +
            "left join ARBEITSVERTRAG_ZUSATZ_FIX on ARBEITSVERTRAG_ZUSATZ.id = ARBEITSVERTRAG_ZUSATZ_FIX.ARBEITSVERTRAG_ZUSATZ_id " +
            "left join DPW_ARBEITSPLANMODELL on ARBEITSVERTRAG_ZUSATZ_FIX.Arbeitsplanmodell = DPW_ARBEITSPLANMODELL.id " +
            "join ARBEITSVERTRAG_NICHTLEISTUNGEN an on an.ARBEITSVERTRAG_id = ARBEITSVERTRAG.id " +
            "inner join STANDORT on STANDORT.SOstandortid = ARBEITSVERTRAG_ZUSATZ.dienstort " +
            "join BENUTZER bn on bn.BNadnr = ARBEITSVERTRAG.ADRESSE_adnr " +
            "where  bn.BNadSAMAN like :user " +
            "order by earliest_datum_von " +
            ") select v_arbeitsvertrag.earliest_datum_von as datum_von, " +
            "         v_arbeitsvertrag.latest_datum_bis as datum_bis, " +
            "         v_arbeitsvertrag.VERWENDUNGSGRUPPE, " +
            "         v_arbeitsvertrag.STUFE, " +
            "         v_arbeitsvertrag.FUNKTION, " +
            "         v_arbeitsvertrag.KST, " +
            "         v_arbeitsvertrag.PERSONAL_NUMMER, " +
            "         NULL as EINTRITTSDATUM, " +
            "         v_arbeitsvertrag.wochenstunden, " +
            "         v_arbeitsvertrag.ARBEITSZEITMODEL, " +
            "         v_arbeitsvertrag.DIENSTORT, " +
            "         v_arbeitsvertrag.NICHTLEISTUNGEN " +
            "from v_arbeitsvertrag;";

    String GET_ALL_DIENSTORT = "select distinct(st.SObezeichnung) from ARBEITSVERTRAG_ZUSATZ az\n" +
            "join STANDORT as st on st.SOstandortid = az.dienstort where (st.SOverfuegbarbis is null or st.SOverfuegbarbis > current_date)";

    String GET_ALL_KOSTENSTELLEN = "select distinct(geschaeftsbereich) as kostenstelle from ARBEITSVERTRAG_ZUSATZ \n" +
            "where geschaeftsbereich is not null order by geschaeftsbereich;";

    String FIND_DIENSTORT_BY_BEZEICHNUNG = "select * from STANDORT where SObezeichnung like %:bezeichnung%";

    @Query(value = CONTRACT_QUERY, nativeQuery = true)
    List<Object[]> getContractsDataRaw(String user);

    @Query(value = CONTRACT_WITHOUT_LEISTUNGEN_QUERY, nativeQuery = true)
    List<Object[]> getContractsWithoutLeistungen(String user);

    @Query(value = GET_ALL_DIENSTORT, nativeQuery = true)
    List<String> getAllDienstort();

    @Query(value = GET_ALL_KOSTENSTELLEN, nativeQuery = true)
    List<String> getAllKonstenstellen();

    @Query(value = FIND_DIENSTORT_BY_BEZEICHNUNG, nativeQuery = true)
    List<Object[]> findDienstortByBezeichnung(String bezeichnung);

    List<ArbeitsvertragIbos> findAllByAddressNo(Integer addreseId);

    List<ArbeitsvertragIbos> findAllByEruserAndErdaAfterOrEruserAndAedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);
}

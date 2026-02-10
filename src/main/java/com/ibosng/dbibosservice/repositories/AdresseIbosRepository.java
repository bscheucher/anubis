package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.AdresseIbos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface AdresseIbosRepository extends JpaRepository<AdresseIbos, Integer> {

    String TN_SEARCH_QUERY = "select distinct ad.* from ADRESSE ad " +
            "join SM_TN sm_tn on ad.ADadnr = sm_tn.ADRESSE_ADadnr " +
            "join SEMINAR sm on sm.SMnr = sm_tn.SEMINAR_SMnr " +
            "join PROJEKT pj on pj.PJnr = sm.PJnr " +
            "join PROJEKTTYPEN pj_ty on pj_ty.id = pj.PJtyp " +
            "where ad.ADtyp = 'tn' " +
            "and pj_ty.bezeichnung in ('ÜBA NÖ', 'ÜBA Wien','ÜBA Tirol','ÜBA OÖ','ÜBA Salzburg') " +
            "and (:searchTerm is null or " +
            "LOWER(ad.ADvnf2) like CONCAT('%', LOWER(:searchTerm), '%') " +
            "or LOWER(ad.ADznf1) like CONCAT('%', LOWER(:searchTerm), '%') " +
            "or LOWER(REPLACE(ad.ADsvnr, ' ', '')) like CONCAT('%', LOWER(:searchTerm), '%') " +
            "or LOWER(ad.ADpersnr) like CONCAT('%', LOWER(:searchTerm), '%')) " +
            "order by " +
            "case when :sortBy = 'vorname' then ad.ADvnf2 " +
            "     when :sortBy = 'nachname' then ad.ADznf1 " +
            "     when :sortBy = 'svNummer' then ad.ADsvnr " +
            "     when :sortBy = 'personalnummer' then ad.ADpersnr " +
            "end ";


    String FIND_VALID_ADRESSE_FROM_WORK_EMAIL = """
                    SELECT distinct ad.*\s
                    FROM ADRESSE ad
                    JOIN ARBEITSVERTRAG av ON av.ADRESSE_adnr = ad.ADadnr
                    JOIN ARBEITSVERTRAG_ZUSATZ avz\s
                        ON avz.ARBEITSVERTRAG_id = av.id\s
                        AND avz.datum_von < CURRENT_DATE\s
                        AND (avz.datum_bis IS NULL OR avz.datum_bis > CURRENT_DATE)\s
                        AND avz.persnr IS NOT NULL\s
                        AND avz.persnr != ''\s
                    WHERE ad.ADemail2 = :workEmail\
            """;

    String FIND_VALID_ADRESSE_FROM_WORK_EMAIL_AND_VERTRAG_FIX = """
            SELECT distinct ad.*
                    FROM ADRESSE ad
                    JOIN ARBEITSVERTRAG_FIX avf on ad.ADadnr = avf.vorgesetzter_id
                    JOIN ARBEITSVERTRAG av ON avf.ARBEITSVERTRAG_id = av.id
                    JOIN ARBEITSVERTRAG_ZUSATZ avz
                        ON avz.ARBEITSVERTRAG_id = av.id
                        AND avz.datum_von < CURRENT_DATE
                        AND (avz.datum_bis IS NULL OR avz.datum_bis > CURRENT_DATE)
                        AND avz.persnr IS NOT NULL
                        AND avz.persnr != ''
                    WHERE ad.ADemail2 = :workEmail""";

    String FIND_ADRESSE_FROM_PERSONALNUMMER = """
            select ad.* from ADRESSE ad
                join ARBEITSVERTRAG av on av.ADRESSE_adnr = ad.ADadnr
                     join ARBEITSVERTRAG_ZUSATZ avz on avz.ARBEITSVERTRAG_id = av.id and avz.datum_von < current_date and (avz.datum_bis is null OR avz.datum_bis > current_date) and persnr is not null and persnr = :personalnummer
                     join IBIS_FIRMA ifi on ifi.id = avz.dienstgeber and ifi.lhr_nr = :ibisFirma
            order by avz.persnr desc;""";

    String FIND_ACTIVE_FUEHRUNGSKRAEFTE = """
            select distinct ad.ADemail2 as email from ARBEITSVERTRAG_FIX avf
                join ARBEITSVERTRAG av on avf.ARBEITSVERTRAG_id = av.id
                        JOIN ARBEITSVERTRAG_ZUSATZ avz
                        ON avz.ARBEITSVERTRAG_id = av.id
                        AND avz.datum_von < CURRENT_DATE
                        AND avz.persnr IS NOT NULL
                        AND avz.persnr != ''
                AND (avz.datum_bis IS NULL OR avz.datum_bis > CURRENT_DATE)
            join ADRESSE ad on ad.ADadnr = avf.vorgesetzter_id and ad.ADemail2 is not null order by ad.ADvnf2""";

    String FIND_ACTIVE_STARTCOACHES = """
            select distinct ad.ADemail2 as email from ARBEITSVERTRAG_FIX avf
                    join ARBEITSVERTRAG av on avf.ARBEITSVERTRAG_id = av.id
                        JOIN ARBEITSVERTRAG_ZUSATZ avz
                        ON avz.ARBEITSVERTRAG_id = av.id
                        AND avz.datum_von < CURRENT_DATE
                        AND avz.persnr IS NOT NULL
                        AND avz.persnr != ''
                AND (avz.datum_bis IS NULL OR avz.datum_bis > CURRENT_DATE)
            join ADRESSE ad on ad.ADadnr = avf.startcoach_id and ad.ADemail2 is not null order by ad.ADvnf2""";

    String FIND_FUEHRUNGSKRAFT_FROM_EMAIL = "select ADemail2 from ADRESSE where ADadnr = (select get_vorgesetzter(ADadnr,date(now())) from ADRESSE where ADemail2 = :email AND get_vorgesetzter(ADadnr, DATE(NOW())) IS NOT NULL)";

    String FIND_FUEHRUNGSKRAFT_FROM_LOGIN = "select ADemail2 from ADRESSE where ADadnr = (select get_vorgesetzter(BNadnr,date(now())) from BENUTZER where BNadSAMAN = :login AND get_vorgesetzter(BNadnr,date(now())) IS NOT NULL)";

    String FIND_FUEHRUNGSKRAFT_UPN_FROM_LOGIN = "select BNupn from BENUTZER where BNadnr = (select ADadnr from ADRESSE where ADadnr = (select get_vorgesetzter(BNadnr,date(now())) from BENUTZER where BNadSAMAN = :login AND get_vorgesetzter(BNadnr,date(now())) IS NOT NULL))";

    String FIND_ADRESSE_FROM_UPN = "select ben.BNadnr from BENUTZER ben where ben.BNupn = :upn";

    String FIND_ALL_ADRESSE_IN_WITH_SEMINAR_DATA = """
            select adr.* from ADRESSE adr where adr.ADadnr in (select sm2tn.ADRESSE_ADadnr from SM_TN sm2tn where (sm2tn.TAeruser = :createdBy
                   AND sm2tn.TAerda >= :after)
               OR (sm2tn.TAeruser = :createdBy
                   AND sm2tn.TAaeda >= :after));""";

    String FIND_ALL_CHANGED_IN_THE_LAST_TIMESTAMP = "select * from ADRESSE adr where " +
            "(adr.ADeruser = :eruser1 AND adr.ADerda >= :timeErda AND adr.ADtyp = :adtyp1) OR (adr.ADeruser = :eruser2 AND adr.ADaeda >= :timeAeda AND adr.ADtyp = :adtyp2);";

    String FIND_LEITER_KOSTENSTELLE_BY_UPN = "SELECT CONCAT((SELECT BNupn FROM BENUTZER WHERE BNadnr = KSTbereichsleiter), '@ibisacam.co.at') AS benutzerUpn " +
            "FROM FKOSTENSTELLE " +
            "WHERE KSTKSTNR = 0 AND KSTKSTSUB = 0 AND KSTKSTGR = :id";


    @Query(value = FIND_LEITER_KOSTENSTELLE_BY_UPN, nativeQuery = true)
    String findKostenstelle2UpnByKostenstelleId(@Param("id") int id);

    @Query(value = TN_SEARCH_QUERY + " ASC", nativeQuery = true)
    List<Object[]> getFilteredTeilnehmerAsc(String searchTerm, String sortBy);

    @Query(value = TN_SEARCH_QUERY + " DESC", nativeQuery = true)
    List<Object[]> getFilteredTeilnehmerDesc(String searchTerm, String sortBy);

    Page<AdresseIbos> findAllByAderdaAfterOrAdaedaAfter(LocalDateTime timeErda, LocalDateTime timeAeda, Pageable pageable);

    List<AdresseIbos> findAllByAdaedaAfterAndAdtyp(LocalDateTime timeAeda, String adtyp);

    Page<AdresseIbos> findAllByAderdaAfterAndAdtypOrAdaedaAfterAndAdtyp(LocalDateTime timeErda, String adtyp1, LocalDateTime timeAeda, String adtyp2, Pageable pageable);

    Page<AdresseIbos> findAllByAderuser(String createdBy, Pageable pageable);

    //    Page<AdresseIbos> findAllByAderuserAndAderdaOrAderuserAndAdaeda(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda, Pageable pageable);
    List<AdresseIbos> findAllByAderuserAndAderdaAfterOrAderuserAndAdaedaAfter(String eruser1, LocalDateTime timeErda, String eruser2, LocalDateTime timeAeda);

    List<AdresseIbos> findAllByAderuserAndAderdaAfterAndAdtypOrAderuserAndAdaedaAfterAndAdtyp(String eruser1, LocalDateTime timeErda, String adtyp1, String eruser2, LocalDateTime timeAeda, String adtyp2);

    @Query(value = FIND_ALL_CHANGED_IN_THE_LAST_TIMESTAMP, nativeQuery = true)
    List<AdresseIbos> findAllChangedInTheLastTimestamp(String eruser1, LocalDateTime timeErda, String adtyp1, String eruser2, LocalDateTime timeAeda, String adtyp2);

    List<AdresseIbos> findAllByAdpersnrAndAderuser(String personalnummer, String eruser);

    List<AdresseIbos> findAllByAdvnf2AndAdznf1(String vorname, String nachname);

    List<AdresseIbos> findAllByAdemail1(String email);

    List<AdresseIbos> findAllByAdemail2(String email);

    AdresseIbos findOneByAdvnf2AndAdznf1AndAdsvnrAndAderuser(String advnf2, String adznf1, String adsvnr, String aderuser);

    @Query(value = FIND_VALID_ADRESSE_FROM_WORK_EMAIL, nativeQuery = true)
    List<AdresseIbos> findValidAdresseFromWorkEmail(String workEmail);

    @Query(value = FIND_VALID_ADRESSE_FROM_WORK_EMAIL_AND_VERTRAG_FIX, nativeQuery = true)
    List<AdresseIbos> findValidAdresseFromWorkEmailAndVertragFix(String workEmail);

    @Query(value = FIND_ADRESSE_FROM_PERSONALNUMMER, nativeQuery = true)
    List<AdresseIbos> findAdresseIbosFromPersonalnummer(String personalnummer, Integer ibisFirma);

    @Query(value = FIND_ACTIVE_FUEHRUNGSKRAEFTE, nativeQuery = true)
    List<String> findEmailsOfActiveFuehrungskraefte();

    @Query(value = FIND_ACTIVE_STARTCOACHES, nativeQuery = true)
    List<String> findEmailsOfActiveStartcoaches();

    @Query(value = FIND_FUEHRUNGSKRAFT_FROM_EMAIL, nativeQuery = true)
    String getFuehrungskraftFromEmail(String email);

    @Query(value = FIND_FUEHRUNGSKRAFT_FROM_LOGIN, nativeQuery = true)
    String getFuehrungskraftFromLogin(String login);

    @Query(value = FIND_FUEHRUNGSKRAFT_UPN_FROM_LOGIN, nativeQuery = true)
    String getFuehrungskraftUPNFromLogin(String login);

    @Query(value = FIND_ADRESSE_FROM_UPN, nativeQuery = true)
    String getAdresseFromUPN(String upn);

    @Query(value = "select ad from AdresseIbos ad where ad.adadnr in (:adadnrs)")
    List<AdresseIbos> findAllByAdadnrIn(List<Integer> adadnrs);

    @Query(value = FIND_ALL_ADRESSE_IN_WITH_SEMINAR_DATA, nativeQuery = true)
    List<AdresseIbos> findAllChangedInSeminarData(String createdBy, LocalDate after);
}

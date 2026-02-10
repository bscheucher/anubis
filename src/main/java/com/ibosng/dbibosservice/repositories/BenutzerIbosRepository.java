package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.BenutzerIbos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional("mariaDbTransactionManager")
public interface BenutzerIbosRepository extends JpaRepository<BenutzerIbos, Integer> {

    String FIND_ROLES_FOR_USER = """
            select rg.RGname from BENUTZER bn
            join ibos.RECHTGRUPPE_BENUTZER rb on bn.BNid = rb.RGB_BNid
            join RECHTGRUPPE rg on rb.RGB_RGid = rg.RGid
            where bn.BNadSAMAN = :user""";

    String FIND_ACTIVE_PROJECTS_FOR_USER = """
            select p.PJnr, CONCAT(LPAD(p.PJkstgr, 2, '0'), ' ', LPAD(p.PJkstnr, 2, '0'), ' ', LPAD(p.PJkostentraeger, 3, '0'), ' ', p.PJbezeichnung1) from BENUTZER bn
                        left join PROJEKT_PK pk on pk.ADRESSE_id = bn.BNadnr
                        inner join RECHTGRUPPE_BENUTZER rb on bn.BNid = rb.RGB_BNid
                        inner join RECHTGRUPPE rg on rb.RGB_RGid = rg.RGid
                        inner join RECHTGRUPPE_BENUTZER_KST rbk on rb.RGBid = rbk.RGBK_RGBid
                        inner join PROJEKT p on (p.PJkstgr = rbk.RGBK_KSTGR and p.PJkstnr = RGBK_KSTNR) or (pk.PROJEKT_id =p.PJnr) or (p.PJkstgr = rbk.RGBK_KSTGR and RGBK_KSTNR = '0')
                        where (rg.RGname = 'PK' or rg.RGname = 'CFO') and CURRENT_DATE between p.PJdatumvon and p.PJdatumbis
                        and bn.bnadSaman = :user
                        group by p.PJnr""";

    String FIND_PAST_PROJECTS_FOR_USER = """
            select p.PJnr, CONCAT(LPAD(p.PJkstgr, 2, '0'), ' ', LPAD(p.PJkstnr, 2, '0'), ' ', LPAD(p.PJkostentraeger, 3, '0'), ' ', p.PJbezeichnung1) from BENUTZER bn
                        left join PROJEKT_PK pk on pk.ADRESSE_id = bn.BNadnr
                        inner join RECHTGRUPPE_BENUTZER rb on bn.BNid = rb.RGB_BNid
                        inner join RECHTGRUPPE rg on rb.RGB_RGid = rg.RGid
                        inner join RECHTGRUPPE_BENUTZER_KST rbk on rb.RGBid = rbk.RGBK_RGBid
                        inner join PROJEKT p on (p.PJkstgr = rbk.RGBK_KSTGR and p.PJkstnr = RGBK_KSTNR) or (pk.PROJEKT_id =p.PJnr) or (p.PJkstgr = rbk.RGBK_KSTGR and RGBK_KSTNR = '0')
                        where (rg.RGname = 'PK' or rg.RGname = 'CFO') and p.PJdatumbis < CURRENT_DATE
                        and bn.bnadSaman = :user
                        group by p.PJnr""";


    String FIND_FUTURE_PROJECTS_FOR_USER = """
            select p.PJnr, CONCAT(LPAD(p.PJkstgr, 2, '0'), ' ', LPAD(p.PJkstnr, 2, '0'), ' ', LPAD(p.PJkostentraeger, 3, '0'), ' ', p.PJbezeichnung1) from BENUTZER bn
                        left join PROJEKT_PK pk on pk.ADRESSE_id = bn.BNadnr
                        inner join RECHTGRUPPE_BENUTZER rb on bn.BNid = rb.RGB_BNid
                        inner join RECHTGRUPPE rg on rb.RGB_RGid = rg.RGid
                        inner join RECHTGRUPPE_BENUTZER_KST rbk on rb.RGBid = rbk.RGBK_RGBid
                        inner join PROJEKT p on (p.PJkstgr = rbk.RGBK_KSTGR and p.PJkstnr = RGBK_KSTNR) or (pk.PROJEKT_id =p.PJnr) or (p.PJkstgr = rbk.RGBK_KSTGR and RGBK_KSTNR = '0')
                        where (rg.RGname = 'PK' or rg.RGname = 'CFO') and p.PJdatumvon > CURRENT_DATE
                        and bn.bnadSaman = :user
                        group by p.PJnr""";

    String FIND_IS_AND_SOLL_REVENUE_BETWEEN_DATES = """
            select pl.PJnr , pl.PJdatumvon , pl.PJdatumbis, pl.PreisP as SollUmsatz , pl.PMNH as SollStunden , sum(pl.sumdauer) as IstStunden , (sum(pl.sumdauer) * pl.PStundenSatz ) as istUmsatz, pl.PStundenSatz as stundenSatz from (
            select  PROJEKT.PJnr,PROJEKT.PJdatumvon, PROJEKT.PJdatumbis, PROJEKT.PJprojektpreis as PreisP , PROJEKT.PJprojektMNH as PMNH,  (PJprojektpreis / PJprojektMNH )as PStundenSatz, PZLEISTUNG.LZdauer , sum(PZLEISTUNG.LZdauer) as sumdauer
            from PROJEKT
            inner join SEMINAR on SEMINAR.PJnr = PROJEKT.PJnr
            inner join PZLEISTUNG on PZLEISTUNG.LZSMnr = SEMINAR.SMnr and PZLEISTUNG.LZtaetnr in (4567 , 4603 , 3333 , 3831 , 3339)
            where PROJEKT.PJnr = :projectNumber
            and PZLEISTUNG.LZdatum >= :von
            and PZLEISTUNG.LZdatum <= :bis
            group by PZLEISTUNG.LZdatum) as pl""";

    String FIND_PLAN_REVENUE_BETWEEN_DATES = """
            select sum(pp.pTdauer) as planStunden, (sum(pp.pTdauer) * pp.PStundenSatz ) as planUmsatz, pp.soll_stunden as SollStunden, pp.soll_umsatz as SollUmsatz from (
            select (PJprojektpreis / PJprojektMNH ) as PStundenSatz, BELEGUNG_POS.BPeinheiten, BELEGUNG_POS.BPeinheiten as pTdauer, PROJEKT.PJprojektpreis as soll_umsatz, PROJEKT.PJprojektMNH as soll_stunden
            from PROJEKT
            inner join SEMINAR on SEMINAR.PJnr = PROJEKT.PJnr
            inner join BELEGUNG on BELEGUNG.BK_SMnr = SEMINAR.SMnr
            inner join BELEGUNG_POS on BELEGUNG_POS.BKnr = BELEGUNG.BKnr
            where PROJEKT.PJnr = :projectNumber
            and BKtyp = 'tr'
            and BPurlaub = 'n'
            and BELEGUNG_POS.BPdatum >= :von
            and BELEGUNG_POS.BPdatum <= :bis
            )as pp""";

    String FIND_FORECAST_REVENUE = """
            select pp.PreisP as SollUmsatz , pp.PMNH as SollStunden,  sum(pp.pTdauer) as PlanStunden , (sum(pp.pTdauer) * pp.PStundenSatz ) as planUmsatz  from (
            select PROJEKT.PJprojektpreis as PreisP , PROJEKT.PJprojektMNH as PMNH, (PJprojektpreis / PJprojektMNH )as PStundenSatz, BELEGUNG_POS.BPeinheiten
            , BELEGUNG_POS.BPeinheiten as pTdauer
            from PROJEKT
            inner join SEMINAR on SEMINAR.PJnr = PROJEKT.PJnr
            inner join BELEGUNG on BELEGUNG.BK_SMnr = SEMINAR.SMnr
            inner join BELEGUNG_POS on BELEGUNG_POS.BKnr = BELEGUNG.BKnr
            where PROJEKT.PJnr = :projectNumber
            and BKtyp = 'tr'
            and BPurlaub = 'n'
            )as pp""";

    String FIND_PROJECT_SEMINAR_GREATEST_END_DATE = """
            select greatest(max(pr.PJdatumbis), max(sem.SMdatumVon)) as andy from PROJEKT pr
            join SEMINAR sem on sem.PJnr = pr.PJnr
            where pr.PJnr = :projectNumber""";

    String GET_ALL_FUEHRUNGSKRAFT_FOR_MITARBEITER = "select distinct CONCAT(ad.ADvnf2, ' ', ad.ADznf1) as name, ad.ADadnr as id from ARBEITSVERTRAG_FIX avf\n" +
            "join ADRESSE ad on ad.ADadnr = avf.vorgesetzter_id order by ad.ADvnf2";

    String GET_ALL_STARTCOACH_FOR_MITARBEITER = "select distinct CONCAT(ad.ADvnf2, ' ', ad.ADznf1) as name, ad.ADadnr as id from ARBEITSVERTRAG_FIX avf\n" +
            "join ADRESSE ad on ad.ADadnr = avf.startcoach_id order by ad.ADvnf2";

    String GET_EMAIL_FOR_FUEHRUNGSKRAFT_FOR_MITARBEITER = "select distinct ad.ADemail2  from ARBEITSVERTRAG_FIX avf\n" +
            " join ADRESSE ad on ad.ADadnr = avf.vorgesetzter_id where ad.ADadnr = :adresseId order by ad.ADvnf2";

    String GET_EMAIL_FOR_STARTCOACH_FOR_MITARBEITER = "select distinct ad.ADemail2 from ARBEITSVERTRAG_FIX avf\n" +
            " join ADRESSE ad on ad.ADadnr = avf.startcoach_id where ad.ADadnr = :adresseId order by ad.ADvnf2";

    String GET_TITEL_ID = "select id from TITEL where bez_kurz = :titel";

    String SAVE_TITEL = "insert into TITEL (bez_kurz) values (:titel) returning id";

    String GET_TITEL_FROM_ID = "select bez_kurz from TITEL where id = :id";

    String GET_SIGNEES_FROM_KOSTENSTELLE = """
            select distinct ben.BNadSAMAN from FKOSTENSTELLE fko
            join BENUTZER ben on ben.BNadnr = fko.KSTbereichsleiter
            where fko.KSTKSTNR = 0 and fko.KSTKSTSUB = 0 and fko.KSTKSTGR = :kostenstelle""";

    String GET_BENUTZER_FROM_BNADN = "select * from BENUTZER ben where ben.BNadnr <> 0 and ben.BNadnr is not null and ben.BNupn is not null and ben.BNadnr = :bnadrn";

    @Query(value = FIND_ROLES_FOR_USER, nativeQuery = true)
    List<String> findRolesForUser(String user);

    @Query(value = FIND_ACTIVE_PROJECTS_FOR_USER, nativeQuery = true)
    List<Object[]> findActiveProjectsForUser(String user);

    @Query(value = FIND_PAST_PROJECTS_FOR_USER, nativeQuery = true)
    List<Object[]> findPastProjectsForUser(String user);

    @Query(value = FIND_FUTURE_PROJECTS_FOR_USER, nativeQuery = true)
    List<Object[]> findFutureProjectsForUser(String user);

    @Query(value = FIND_IS_AND_SOLL_REVENUE_BETWEEN_DATES, nativeQuery = true)
    List<Object[]> findIsAndSollRevenueBetweenDates(Integer projectNumber, LocalDate von, LocalDate bis);

    @Query(value = FIND_PLAN_REVENUE_BETWEEN_DATES, nativeQuery = true)
    List<Object[]> findPlanRevenueBetweenDates(Integer projectNumber, LocalDate von, LocalDate bis);

    @Query(value = FIND_FORECAST_REVENUE, nativeQuery = true)
    List<Object[]> findForecastRevenue(Integer projectNumber);

    @Query(value = FIND_PROJECT_SEMINAR_GREATEST_END_DATE, nativeQuery = true)
    LocalDate findProjectSeminarGreatestEndDate(Integer projectNumber);

    @Query(value = GET_ALL_FUEHRUNGSKRAFT_FOR_MITARBEITER, nativeQuery = true)
    List<Object[]> getAllFuehrungskraftForMitarbeiter();

    @Query(value = GET_ALL_STARTCOACH_FOR_MITARBEITER, nativeQuery = true)
    List<Object[]> getAllStartcoachForMitarbeiter();

    @Query(value = GET_EMAIL_FOR_FUEHRUNGSKRAFT_FOR_MITARBEITER, nativeQuery = true)
    String getEmailForFuehrungskraftForMitarbeiter(Long adresseId);

    @Query(value = GET_EMAIL_FOR_STARTCOACH_FOR_MITARBEITER, nativeQuery = true)
    String getEmailForStartcoachForMitarbeiter(Long adresseId);

    @Query(value = GET_TITEL_ID, nativeQuery = true)
    Integer getTitelId(String titel);

    @Query(value = SAVE_TITEL, nativeQuery = true)
    Integer saveTitel(String titel);

    @Query(value = GET_TITEL_FROM_ID, nativeQuery = true)
    String getTitelFromId(Integer id);

    @Query(value = GET_SIGNEES_FROM_KOSTENSTELLE, nativeQuery = true)
    List<String> getSigneesFromKostenStelle(Integer kostenstelle);

    @Query(nativeQuery = true, value = GET_BENUTZER_FROM_BNADN)
    BenutzerIbos findBenutzerByBnadnr(Integer bnadrn);
}

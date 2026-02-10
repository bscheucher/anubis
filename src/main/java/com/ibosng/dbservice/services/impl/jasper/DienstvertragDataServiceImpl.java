package com.ibosng.dbservice.services.impl.jasper;

import com.ibosng.dbservice.entities.reports.DienstvertragDataDto;
import com.ibosng.dbservice.services.jasper.DienstvertragDataService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DienstvertragDataServiceImpl implements DienstvertragDataService {

    private final EntityManagerFactory entityManagerFactory;

    String DIENSTVERTRAG_QUERY = "select pn.personalnummer                                               as pnr,\n" +
            "       stm.id,\n" +
            "       ibf.name                                                        as firmenname,\n" +
            "       anr.name                                                        as anrede,\n" +
            "       stm.vorname                                                     AS VORNAME,\n" +
            "       stm.nachname                                                    AS NACHNAME,\n" +
            "       tt1.name                                                        as TITEL1,\n" +
            "       tt1.position                                                    as titel1_position,\n" +
            "       tt2.name                                                        as TITEL2,\n" +
            "       tt2.position                                                    as titel2_position,\n" +
            "       stm.svnr                                                        AS SVNR,\n" +
            "       stm.geburtsdatum                                                AS GEBURTSDATUM,\n" +
            "       ad.strasse                                                      as STRASSE,\n" +
            " CASE\n" +
            "  WHEN position(',' IN ad.ort) > 0 THEN substring(ad.ort FROM 1 FOR position(',' IN ad.ort) - 1)\n" +
            "  ELSE ad.ort\n" +
            "END AS ort,\n" +
            "       pl.plz                                                          as PLZ,\n" +
            "       la.land_name                                                    AS LAND,\n" +
            "       vg.eintritt                                                     as EINTRITTSDATUM,\n" +
            "       concat(tel_land.telefonvorwahl, tel.telefonnummer)              as telefonnummer,\n" +
            "       staat.land_name                                                 AS NATIONALITAET,\n" +
            "       case when staat.is_in_eu_eea_ch = true then false else true end as drittstaat,\n" +
            "       zus_info.arbeitsgenehmigung                                     as arbeitsgenehmigung,\n" +
            "       zus_info.gueltigbis                                             as gueltigbis,\n" +
            "       case when vg.befristung_bis is null then false else true end    as befristung,\n" +
            "       vg.befristung_bis                                               as befristung_bis,\n" +
            "       concat(adr_dienst_ort.strasse, ', ', plz_dienst_ort.plz, ' ',\n" +
            "              plz_dienst_ort.ort)                                      as dienstort,\n" +
            "       case\n" +
            "           when pl.bundesland = plz_dienst_ort.bundesland then false\n" +
            "           else true end                                               as andereeinsatz, --TODO: THIS HAS TO BE CHANGED AFTER THE BUG!!! OGNEN!!!\n" +
            "       bun.name                                                        as einsatzgebiet,\n" +
            "       taet.name                                                       as taetigkeit,\n" +
            "       job_bes.name                                                    as job_bezeichnung,\n" +
            "       kat.name                                                        as kategorie,\n" +
            "       koll.name                                                       as kollektivvertrag,\n" +
            "       kv_st.name                                                      as kv_stufe,\n" +
            "       ver_gruppe.name                                                 as verwendungsgruppe,\n" +
            "       vor_dien_zeit.firma                                             as vor_zeit_firma,\n" +
            "       vor_dien_zeit.anrechenbar                                       as vor_zeit_anrechenbar,\n" +
            "       vor_dien_zeit.von                                               as vor_zeit_von,\n" +
            "       vor_dien_zeit.bis                                               as vor_zeit_bis,\n" +
            "       vor_dien_zeit.wochenstunden                                     as vor_zeit_wochenstunden,\n" +
            "       vor_dien_zeit_art.name                                          as vor_zeit_vertragsart,\n" +
            "       --vor_dien_zeit.facheinschlaegig                                  as monate_facheinschlaegig,\n" +
            "       geh_info.angerechnete_facheinschlaegige_taetigkeiten_monate     as vor_zeit_sum_mon,\n" +
            "       geh_info.kv_gehalt_berechnet \t\t\t\t\t\t\t\tas kv_gehalt,\n" +
            "       geh_info.gesamt_brutto                                          as gesamt_brutto,\n" +
            "       geh_info.gehalt_vereinbart                                      as gehalt_vereinbart,\n" +
            "       geh_info_zul.art_der_zulage                                     as art_der_zulage,\n" +
            "       geh_info_zul.zulage_in_euro                                     as eur_zulage,\n" +
            "       case\n" +
            "           when geh_info_zul.zulage_in_euro > 0 then true\n" +
            "           else false end                                              as is_zulage,\n" +
            "       geh_info.ueberzahlung                                           as ueberzahlung,\n" +
            "       case\n" +
            "           when  geh_info.ueberzahlung <> 0 then true\n" +
            "           else false end                                              as is_ueberzahlung,\n" +
            "       case\n" +
            "           when geh_info.vereinbarung_ueberstunden = 'All in' then true\n" +
            "           else false end                                              as is_all_in,\n" +
            "       bd.bank                                                         as bank,\n" +
            "       bd.iban                                                         as iban,\n" +
            "       bd.bic                                                          as bicm,\n" +
            "       arb_mod.name                                                    as az_model,\n" +
            "       arb_mod.id                                                      as az_model_id,\n" +
            "       arbz.montag_von                                                 as montag_von,\n" +
            "       arbz.montag_bis                                                 as montag_bis,\n" +
            "       arbz.montag_netto                                               as montag_netto,\n" +
            "       arbz.dienstag_von                                               as dienstag_von,\n" +
            "       arbz.dienstag_bis                                               as dienstag_bis,\n" +
            "       arbz.dienstag_netto                                             as dienstag_netto,\n" +
            "       arbz.mittwoch_von                                               as mittwoch_von,\n" +
            "       arbz.mittwoch_bis                                               as mittwoch_bis,\n" +
            "       arbz.mittwoch_netto                                             as mittwoch_netto,\n" +
            "       arbz.donnerstag_von                                             as donnerstag_von,\n" +
            "       arbz.donnerstag_bis                                             as donnerstag_bis,\n" +
            "       arbz.donnerstag_netto                                           as donnerstag_netto,\n" +
            "       arbz.freitag_von                                                as freitag_von,\n" +
            "       arbz.freitag_bis                                                as freitag_bis,\n" +
            "       arbz.freitag_netto                                              as freitag_netto,\n" +
            "       arbz.samstag_von                                                as samstag_von,\n" +
            "       arbz.samstag_bis                                                as samstag_bis,\n" +
            "       arbz.samstag_netto                                              as samstag_netto,\n" +
            "       arb_info.arbeitszeitmodell_von                                  as arbeitszeitmodell_von,\n" +
            "       arb_info.arbeitszeitmodell_bis                                  as arbeitszeitmodell_bis,\n" +
            "       arb_info.wochenstunden                                          as wochenstunden,\n" +
            "       auswahl_begruendung_fuer_durchrechner as dr_begruendung,\n" +
            "       coalesce(arb_info.urlaub_vorab_vereinbart, false)               as urlaub_vorab_vereinbart,\n" +
            "       case\n" +
            "           when taet.name = 'Geschäftsführung' OR taet.name = 'Geschäftsfeldleitung' then true\n" +
            "           else false end                                              as konkurrenz,\n" +
            "       case\n" +
            "           when job_bes.name = 'Betriebskontakterin' then true\n" +
            "           else false end                                              as betriebskontakter,\n" +
            "       true                                                            as erfinde\n" +
            "from stammdaten stm\n" +
            "         left join titel tt1 on tt1.id = stm.titel\n" +
            "         left join titel tt2 on tt2.id = stm.titel2\n" +
            "         join personalnummer pn on pn.id = stm.personalnummer\n" +
            "         join ibis_firma ibf on ibf.id = pn.firma\n" +
            "         left join anrede anr on anr.id = stm.anrede\n" +
            "         join vertragsdaten vg on vg.personalnummer = pn.id\n" +
            "         left join adresse ad on ad.id = stm.adresse\n" +
            "         left join plz pl on pl.id = ad.plz\n" +
            "         left join bundesland bun on bun.id = pl.bundesland\n" +
            "         left join land la on la.id = ad.land\n" +
            "         left join telefon tel on tel.id = stm.mobilnummer\n" +
            "         left join land tel_land on tel_land.id = tel.land_id\n" +
            "         left join land staat on staat.id = stm.staatsbuergerschaft\n" +
            "         left join dienstort dienst_ort on dienst_ort.id = vg.dienstort\n" +
            "         left join adresse adr_dienst_ort on adr_dienst_ort.id = dienst_ort.adresse\n" +
            "         left join plz plz_dienst_ort on plz_dienst_ort.id = adr_dienst_ort.plz\n" +
            "         join taetigkeit taet on taet.id = vg.taetigkeit\n" +
            "         join jobbeschreibung job_bes on job_bes.id = vg.job_bezeichnung\n" +
            "         left join gehalt_info geh_info on geh_info.vertragsdaten_id = vg.id\n" +
            "         join kategorie kat on kat.id = vg.kategorie\n" +
            "         left join kv_stufe kv_st on kv_st.id = geh_info.stufe\n" +
            "         left join verwendungsgruppe ver_gruppe on ver_gruppe.id = geh_info.verwendungsgruppe\n" +
            "         left join kollektivvertrag koll on koll.id = ver_gruppe.kollektivvertrag\n" +
            "         left join vordienstzeiten vor_dien_zeit on vor_dien_zeit.vertragsdaten_id = vg.id\n" +
            "         left join vertragsart vor_dien_zeit_art on vor_dien_zeit_art.id = vor_dien_zeit.vertragsart\n" +
            "         join bank_daten bd on bd.id = stm.bank\n" +
            "         join arbeitszeiten_info arb_info on arb_info.vertragsdaten_id = vg.id\n" +
            "         join arbeitszeiten arbz on arbz.arbeitszeiten_info_id = arb_info.id\n" +
            "         left join arbeitszeitmodell arb_mod on arb_mod.id = arb_info.arbeitszeitmodell\n" +
            "         left join zusatz_info zus_info on zus_info.id = stm.zusatz_info\n" +
            "         left join gehalt_info_zulage geh_info_zul on geh_info.id = geh_info_zul.gehalt_info_id\n" +
            "where stm.vorname is not null\n" +
            "  and stm.nachname is not null\n" +
            "  and stm.geburtsdatum is not null\n" +
            "  and ad.strasse is not null\n" +
            "  and ad.ort is not null\n" +
            " -- and pl.plz is not null\n" +
            "  and la.land_name is not null\n" +
            "  and vg.eintritt is not null\n" +
            "  and tel.telefonnummer is not null\n" +
            "  and stm.staatsbuergerschaft is not null\n" +
            "  and staat.land_name is not null\n" +
            "  and pn.personalnummer = :personalnummer \n" +
            "  limit 1";


    public DienstvertragDataServiceImpl(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;

    }

    @Override
    public List<DienstvertragDataDto> getDvTestData(String personalnummer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createNativeQuery(DIENSTVERTRAG_QUERY);
        query.setParameter("personalnummer", personalnummer);

        List<Object[]> results = query.getResultList();

        return results.stream()
                .map(row -> new DienstvertragDataDto(
                        row[0] != null ? (String) row[0] : null,  // pnr
                        row[1] != null ? (Integer) row[1] : null, // id
                        row[2] != null ? (String) row[2] : null,  // firmenname
                        row[3] != null ? (String) row[3] : null,  // anrede
                        row[4] != null ? (String) row[4] : null,  // vorname
                        row[5] != null ? (String) row[5] : null,  // nachname
                        row[6] != null ? (String) row[6] : null,  // titel1
                        row[7] != null ? ((Short) row[7]).intValue() : null, // titel1_position
                        row[8] != null ? (String) row[8] : null,  // titel2
                        row[9] != null ? ((Short) row[9]).intValue() : null, // titel2_position
                        row[10] != null ? (String) row[10] : null,  // svnr
                        row[11] != null ? (Date) row[11] : null,  // geburtsdatum
                        row[12] != null ? (String) row[12] : null, // strasse
                        row[13] != null ? (String) row[13] : null, // ort
                        row[14] != null ? (Integer) row[14] : null, // plz
                        row[15] != null ? (String) row[15] : null, // land
                        row[16] != null ? (Date) row[16] : null,  // eintrittsdatum
                        row[17] != null ? (String) row[17] : null, // telefonnummer
                        row[18] != null ? (String) row[18] : null, // nationalitaet
                        row[19] != null ? (Boolean) row[19] : null, // drittstaat
                        row[20] != null ? (String) row[20] : null, // arbeitsgenehmigung
                        row[21] != null ? (Date) row[21] : null,  // gueltigbis
                        row[22] != null ? (Boolean) row[22] : null, // befristung
                        row[23] != null ? (Date) row[23] : null,  // befristung_bis
                        row[24] != null ? (String) row[24] : null, // dienstort
                        row[25] != null ? (Boolean) row[25] : null, // andereeinsatz
                        row[26] != null ? (String) row[26] : null, // einsatzgebiet
                        row[27] != null ? (String) row[27] : null, // taetigkeit
                        row[28] != null ? (String) row[28] : null, // job_bezeichnung
                        row[29] != null ? (String) row[29] : null, // kategorie
                        row[30] != null ? (String) row[30] : null, // kollektivvertrag
                        row[31] != null ? (String) row[31] : null, // kv_stufe
                        row[32] != null ? (String) row[32] : null, // verwendungsgruppe
                        row[33] != null ? (String) row[33] : null, // vor_zeit_firma
                        row[34] != null ? (Boolean) row[34] : null, // vor_zeit_anrechenbar
                        row[35] != null ? (Date) row[35] : null,  // vor_zeit_von
                        row[36] != null ? (Date) row[36] : null,  // vor_zeit_bis
                        row[37] != null ? (Double) row[37] : null, // vor_zeit_wochenstunden
                        row[38] != null ? (String) row[38] : null, // vor_zeit_vertragsart
                        row[39] != null ? (Integer) row[39] : null, // vor_zeit_sum_mon
                        row[40] != null ? (BigDecimal) row[40] : null, // kv_gehalt
                        row[41] != null ? (BigDecimal) row[41] : null, // gesamt_brutto
                        row[42] != null ? (BigDecimal) row[42] : null, // gehalt_vereinbart
                        row[43] != null ? (String) row[43] : null, // art_der_zulage
                        row[44] != null ? (BigDecimal) row[44] : null, // eur_zulage
                        row[45] != null ? (Boolean) row[45] : null, // is_zulage
                        row[46] != null ? (BigDecimal) row[46] : null, // ueberzahlung
                        row[47] != null ? (Boolean) row[47] : null, // is_ueberzahlung
                        row[48] != null ? (Boolean) row[48] : null, // is_all_in
                        row[49] != null ? (String) row[49] : null, // bank
                        row[50] != null ? (String) row[50] : null, // iban
                        row[51] != null ? (String) row[51] : null, // bicm
                        row[52] != null ? (String) row[52] : null, // az_model
                        row[53] != null ? (Integer) row[53] : null, // az_model_id
                        row[54] != null ? (Time) row[54] : null,  // montag_von
                        row[55] != null ? (Time) row[55] : null,  // montag_bis
                        row[56] != null ? (Double) row[56] : null, // montag_netto
                        row[57] != null ? (Time) row[57] : null,  // dienstag_von
                        row[58] != null ? (Time) row[58] : null,  // dienstag_bis
                        row[59] != null ? (Double) row[59] : null, // dienstag_netto
                        row[60] != null ? (Time) row[60] : null,  // mittwoch_von
                        row[61] != null ? (Time) row[61] : null,  // mittwoch_bis
                        row[62] != null ? (Double) row[62] : null, // mittwoch_netto
                        row[63] != null ? (Time) row[63] : null,  // donnerstag_von
                        row[64] != null ? (Time) row[64] : null,  // donnerstag_bis
                        row[65] != null ? (Double) row[65] : null, // donnerstag_netto
                        row[66] != null ? (Time) row[66] : null,  // freitag_von
                        row[67] != null ? (Time) row[67] : null,  // freitag_bis
                        row[68] != null ? (Double) row[68] : null, // freitag_netto
                        row[69] != null ? (Time) row[69] : null,  // samstag_von
                        row[70] != null ? (Time) row[70] : null,  // samstag_bis
                        row[71] != null ? (Double) row[71] : null, // samstag_netto
                        row[72] != null ? (Date) row[72] : null,  // arbeitszeitmodell_von
                        row[73] != null ? (Date) row[73] : null,  // arbeitszeitmodell_bis
                        row[74] != null ? (String) row[74] : null, // wochenstunden
                        row[75] != null ? (String) row[75] : null, // dr_begruendung
                        row[76] != null ? (Boolean) row[76] : null, // urlaub_vorab_vereinbart
                        row[77] != null ? (Boolean) row[77] : null, // konkurrenz
                        row[78] != null ? (Boolean) row[78] : null, // betriebskontakter
                        row[79] != null ? (Boolean) row[79] : null  // erfinde
                ))
                .collect(Collectors.toList());


    }
}

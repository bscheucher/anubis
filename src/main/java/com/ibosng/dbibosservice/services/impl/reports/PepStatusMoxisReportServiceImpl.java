package com.ibosng.dbibosservice.services.impl.reports;

import com.ibosng.dbibosservice.dtos.reports.PepStatusMoxisDto;
import com.ibosng.dbibosservice.services.reports.PepStatusMoxisReportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
public class PepStatusMoxisReportServiceImpl implements PepStatusMoxisReportService {

    private final EntityManagerFactory entityManagerFactory;

    private String PEP_STATUS_MOXIS_QUERY = "select\n" +
            "PZMONAT.ADadnr as pepMaId, \n" +
            "(select concat(ADznf1, ' ', ADvnf2) from ADRESSE where ADadnr = PZMONAT.ADadnr) as maName,\n" +
            "(select ADemail2 from ADRESSE where ADadnr = PZMONAT.ADadnr) as maMail,\n" +
            "case when PZMONAT.PMstatus = 0 then 'offen' when PZMONAT.PMstatus >= 1 then 'abgeschlossen' end as pepStatus,\n" +
            "PZMONAT.PMabschlussdatum as abgeschlossenAm, \n" +
            "PZMONAT.PMabschlussbenutzer as abgeschlossenDurch, \n" +
            "PZMONAT.PMjahr as jahr, \n" +
            "PZMONAT.PMmonat as monat, \n" +
            "PZMONAT.PMsoll as soll, \n" +
            "PZMONAT.PMist as pmIst, \n" +
            "PZMONAT.PMurlaub as urlaubKonsumiert, \n" +
            "PZMONAT.PMzakonsum as zaStunden,\n" +
            "laravel_moxis_leistung_processes.kostenstellen_gruppe as kst,\n" +
            "laravel_job_control.dispatched_at as anMoxisVersendet,\n" +
            "laravel_moxis_process_status.id as moxisAuftragNr,\n" +
            "case\n" +
            "when current_iteration = 0 then laravel_moxis_leistung_processes.iteration_0_cfg_adresse_id\n" +
            "when current_iteration = 1 then laravel_moxis_leistung_processes.iteration_1_cfg_adresse_id\n" +
            "when current_iteration = 2 then laravel_moxis_leistung_processes.iteration_2_cfg_adresse_id\n" +
            "when current_iteration = 3 then laravel_moxis_leistung_processes.iteration_3_cfg_adresse_id\n" +
            "when current_iteration = 4 then laravel_moxis_leistung_processes.iteration_4_cfg_adresse_id\n" +
            "end as aktuelleBeiId,\n" +
            "case\n" +
            "when current_iteration = 0 then (select concat(ADvnf2, ' ', ADznf1) from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_0_cfg_adresse_id)\n" +
            "when current_iteration = 1 then (select concat(ADvnf2, ' ', ADznf1) from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_1_cfg_adresse_id)\n" +
            "when current_iteration = 2 then (select concat(ADvnf2, ' ', ADznf1) from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_2_cfg_adresse_id)\n" +
            "when current_iteration = 3 then (select concat(ADvnf2, ' ', ADznf1) from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_3_cfg_adresse_id)\n" +
            "when current_iteration = 4 then (select concat(ADvnf2, ' ', ADznf1) from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_4_cfg_adresse_id)\n" +
            "end as aktuellBeiName,\n" +
            "case\n" +
            "when current_iteration = 0 then (select ADemail2 from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_0_cfg_adresse_id)\n" +
            "when current_iteration = 1 then (select ADemail2 from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_1_cfg_adresse_id)\n" +
            "when current_iteration = 2 then (select ADemail2 from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_2_cfg_adresse_id)\n" +
            "when current_iteration = 3 then (select ADemail2 from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_3_cfg_adresse_id)\n" +
            "when current_iteration = 4 then (select ADemail2 from ADRESSE where ADadnr = laravel_moxis_leistung_processes.iteration_4_cfg_adresse_id)\n" +
            "end as aktuellBeiMail,\n" +
            "case\n" +
            "when current_iteration = 0 then 'Freigabe MA'\n" +
            "when current_iteration = 1 then 'Freigabe Vorgesetzter'\n" +
            "when current_iteration = 2 then 'Unterschrift MA'\n" +
            "when current_iteration = 3 then 'Unterschrift Vorgesetzter'\n" +
            "when current_iteration = 4 then 'Unterschrift Regionalleiter'\n" +
            "when current_iteration is null then 'an Moxis senden'\n" +
            "end as aktuellePosition,\n" +
            "case\n" +
            "when laravel_moxis_process_status.state = 'PROCESSING' then 'in Bearbeitung'\n" +
            "when laravel_moxis_process_status.state = 'FINISHED_SUCCESS' then 'erfolgreich'\n" +
            "when laravel_moxis_process_status.state = 'FINISHED_SIGNATURE_DENIED' then 'fehlgeschlagen'\n" +
            "when laravel_moxis_process_status.state = 'FINISHED_WF_CANCELLED' then 'abgebrochen'\n" +
            "when laravel_moxis_process_status.state = 'FINISHED_TIMEOUT' then 'abgelaufen'\n" +
            "when laravel_moxis_process_status.state = 'POST_PROCESSING' then 'in Bearbeitung'\n" +
            "when laravel_moxis_process_status.state is null then 'kein Moxis Status'\n" +
            "end as moxisStatus,\n" +
            "laravel_moxis_process_status.updated_at as letztesUpdateAm, \n" +
            "laravel_moxis_process_status.ended_at as abgeschlossenAmMoxis, \n" +
            "laravel_moxis_process_status.expires_at as abgelaufenAm\n" +
            "from PZMONAT\n" +
            "inner join DVZUSATZ on DVZUSATZ.DZnr = PMDZnr\n" +
            "inner join DIENSTVERTRAG on DIENSTVERTRAG.DVnr = DVZUSATZ.DVnr and DIENSTVERTRAG.DVkstgr = :kst\n" +
            "left join ibos.laravel_moxis_leistung_processes on laravel_moxis_leistung_processes.constituent_adresse_id = PZMONAT.ADadnr\n" +
            "and laravel_moxis_leistung_processes.year_month = concat(:jahr, :monat)\n" +
            "and laravel_moxis_leistung_processes.kostenstellen_gruppe = :kst\n" +
            "and process_type = 'pep'\n" +
            "and replaced_by_moxis_leistung_process_id is null\n" +
            "left join laravel_moxis_process_status on moxis_process_status_id = laravel_moxis_process_status.id\n" +
            "left join ibos.laravel_job_control on laravel_moxis_process_status.job_control_id = laravel_job_control.id\n" +
            "where PZMONAT.PMjahr = :jahr\n" +
            "and PZMONAT.PMmonat = :monat\n" +
            "and (\n" +
            "(:nichtErfolgreiche = true and (laravel_moxis_process_status.state != 'FINISHED_SUCCESS' or laravel_moxis_process_status.state is null))\n" +
            "or :nichtErfolgreiche = false\n" +
            ")\n" +
            "order by (select concat(ADznf1, ' ', ADvnf2) from ADRESSE where ADadnr = PZMONAT.ADadnr) asc;";


    public PepStatusMoxisReportServiceImpl(@Qualifier("mariaDbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<PepStatusMoxisDto> getPepStatusMoxisData(String jahr, String monat, String kst, boolean nichtErfolgreiche) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createNativeQuery(PEP_STATUS_MOXIS_QUERY);
            query.setParameter("jahr", jahr);
            query.setParameter("monat", monat);
            query.setParameter("kst", kst);
            query.setParameter("nichtErfolgreiche", nichtErfolgreiche);

            // Execute the query
            List<Object[]> results = query.getResultList();

            // Map results to DTOs
            return results.stream().map(this::mapToPepStatusMoxisDto).toList();
        } finally {
            entityManager.close();
        }
    }

    private PepStatusMoxisDto mapToPepStatusMoxisDto(Object[] row) {
        return PepStatusMoxisDto.builder()
                .pepMaId((Long) row[0])
                .maName((String) row[1])
                .maMail((String) row[2])
                .pepStatus((String) row[3])
                .abgeschlossenAm(row[4] != null ? ((Timestamp) row[4]).toLocalDateTime() : null)
                .abgeschlossenDurch((String) row[5])
                .jahr((Integer) row[6])
                .monat((Integer) row[7])
                .soll(row[8] != null ? new BigDecimal(row[8].toString()) : null)
                .pmIst(row[9] != null ? new BigDecimal(row[9].toString()) : null)
                .urlaubKonsumiert((Integer) row[10])
                .zaStunden(row[11] != null ? new BigDecimal(row[11].toString()) : null)
                .kst((String) row[12])
                .anMoxisVersendet(row[13] != null ? ((Timestamp) row[13]).toLocalDateTime() : null)
                .moxisAuftragNr((Long) row[14])
                .aktuelleBeiId((Integer) row[15])
                .aktuellBeiName((String) row[16])
                .aktuellBeiMail((String) row[17])
                .aktuellePosition((String) row[18])
                .moxisStatus((String) row[19])
                .letztesUpdateAm(row[20] != null ? ((Timestamp) row[20]).toLocalDateTime() : null)
                .abgeschlossenAmMoxis(row[21] != null ? ((Timestamp) row[21]).toLocalDateTime() : null)
                .abgelaufenAm(row[22] != null ? ((Timestamp) row[22]).toLocalDateTime() : null)
                .build();
    }

}

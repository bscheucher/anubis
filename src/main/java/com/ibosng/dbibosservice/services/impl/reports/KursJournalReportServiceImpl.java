package com.ibosng.dbibosservice.services.impl.reports;

import com.ibosng.dbibosservice.dtos.reports.KursJournalDto;
import com.ibosng.dbibosservice.services.reports.KursJournalReportService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class KursJournalReportServiceImpl implements KursJournalReportService {

    private final EntityManagerFactory entityManagerFactory;

    private static final String SEMINAR_BUCH_QUERY = """
    select concat('P ',(select ASpnr from AUSSCHREIBUNG where ASnr = PROJEKT.ASnr)) as 'fuerDasProjekt',
           PJbezeichnung1 as 'projektname',
           concat(PJdatumvon,' - ',PJdatumbis) as 'projektzeitraum',
           CONCAT(LPAD(PJkstgr, 2, '0'),  LPAD(PJkstnr, 2, '0'),  LPAD(PJkostentraeger, 3, '0')) as 'kostentraeger',
           (select concat(ADznf1 , ' ', ADvnf2) from ADRESSE where ADRESSE.ADadnr = PZLEISTUNG.ADadnr) as 'trainer',
           SMADbezeichnung as 'fach',
           case when LZtaetnr in('3333','4603','3831') then LZdauer end as 'gesamtSeminar',
           case when LZtaetnr in('4567','3339') then LZdauer end as 'gesamtEcFach',
           case when LZtaetnr in('3333','4603','3831') then 'Gruppentrainer/in'
                when LZtaetnr in('4567') then 'Einzelcoaching'
                when LZtaetnr in('3339') then 'Einzeltraining'
           end as 'stundentyp',
           LZdatum as 'datum',
           LZdatumt as 'uhrzeitVon',
           LZbist as 'uhrzeitBis',
           LZpauseVon as 'pauseVon',
           LZpauseBis as 'pauseBis',
           seminarinhalt as 'inhalt',
           LZbemerk as 'bemerkung',
           LZdauer as 'dauer',
           timestampdiff(MINUTE, LZpauseVon, LZpauseBis) as 'dauerPause',
           (select weekofyear(LZdatum)) as 'kalenderwoche',
           (select weekday(LZdatum)) as 'wochentag'
    from PZLEISTUNG
    left join SEMINAR on SEMINAR.SMnr = PZLEISTUNG.LZSMnr
    inner join PROJEKT on PROJEKT.PJnr = SEMINAR.PJnr
    left join SM_AD on SM_AD.SMADnr = PZLEISTUNG.SM_AD_SMADnr
    left join PZLEISTUNG_SEMINARBUCH_ZUSATZ on pz_leistung_id = PZLEISTUNG.LZnr
    where SEMINAR.PJnr = :projektNr
      and LZtyp = 'l'
      and LZtaetnr in('3333','4603','3831', '4567','3339')
      and (select weekofyear(LZdatum)) = :kalenderwoche
    order by LZdatum, LZtaetnr in('4567','3339'), LZtaetnr in('3333','4603','3831'), LZdatumt;
    """;

    public KursJournalReportServiceImpl(@Qualifier("mariaDbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<KursJournalDto> getSeminarBuchData(Long projektNr, Integer kalenderwoche) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createNativeQuery(SEMINAR_BUCH_QUERY);
            query.setParameter("projektNr", projektNr);
            query.setParameter("kalenderwoche", kalenderwoche);

            List<Object[]> results = query.getResultList();

            return results.stream().map(this::mapToSeminarBuchDto).toList();
        } finally {
            entityManager.close();
        }
    }

//    private SeminarBuchDto mapToSeminarBuchDto(Object[] row) {
//        return SeminarBuchDto.builder()
//                .projektNummer((String) row[0])
//                .projektName((String) row[1])
//                .projektZeitraum((String) row[2])
//                .kostenTraeger((String) row[3])
//                .trainerName((String) row[4])
//                .fach((String) row[5])
//                .gesamtSeminar(row[6] != null ? new BigDecimal(row[6].toString()) : null)
//                .gesamtEcFach(row[7] != null ? new BigDecimal(row[7].toString()) : null)
//                .stundentyp((String) row[8])
//                .datum(row[9] != null ? ((Date) row[9]).toLocalDate() : null)
//                .uhrzeitVon(row[10] != null ? ((Time) row[10]).toLocalTime() : null)
//                .uhrzeitBis(row[11] != null ? ((Time) row[11]).toLocalTime() : null)
//                .pauseVon(row[12] != null ? ((Time) row[12]).toLocalTime() : null)
//                .pauseBis(row[13] != null ? ((Time) row[13]).toLocalTime() : null)
//                .seminarInhalt((String) row[14])
//                .bemerkung((String) row[15])
//                .dauer(row[16] != null ? new BigDecimal(row[16].toString()) : null)
//                .dauerPause((Integer) row[17])
//                .kalenderwoche((Integer) row[18])
//                .wochentag((Integer) row[19])
//                .build();
//    }



    /*private KursJournalDto mapToSeminarBuchDto(Object[] row) {
        KursJournalDto dto = new KursJournalDto();

        dto.setFuerDasProjekt((String) row[0]);
        dto.setProjektname((String) row[1]);
        dto.setProjektzeitraum((String) row[2]);
        dto.setKostentraeger((String) row[3]);
        dto.setTrainer((String) row[4]);
        dto.setFach((String) row[5]);
        dto.setGesamtSeminar(row[6] != null ? new BigDecimal(row[6].toString()) : null);
        dto.setGesamtEcFach(row[7] != null ? new BigDecimal(row[7].toString()) : null);
        dto.setStundentyp((String) row[8]);
        dto.setDatum(row[9] != null ? ((java.sql.Date) row[9]).toLocalDate() : null);
        dto.setUhrzeitVon(row[10] != null ? ((java.sql.Time) row[10]).toLocalTime() : null);
        dto.setUhrzeitBis(row[11] != null ? ((java.sql.Time) row[11]).toLocalTime() : null);
        dto.setPauseVon(row[12] != null ? ((java.sql.Time) row[12]).toLocalTime() : null);
        dto.setPauseBis(row[13] != null ? ((java.sql.Time) row[13]).toLocalTime() : null);
        dto.setInhalt((String) row[14]);
        dto.setBemerkung((String) row[15]);
        dto.setDauer(row[16] != null ? new BigDecimal(row[16].toString()) : null);
        dto.setDauerPause((Integer) row[17]);
        dto.setKalenderwoche((Integer) row[18]);
        dto.setWochentag((Integer) row[19]);

        return dto;
    }*/

    private KursJournalDto mapToSeminarBuchDto(Object[] row) {
        return KursJournalDto.builder()
                .fuerDasProjekt((String) row[0])
                .projektname((String) row[1])
                .projektzeitraum((String) row[2])
                .kostentraeger((String) row[3])
                .trainer((String) row[4])
                .fach((String) row[5])
                .gesamtSeminar(row[6] != null ? new BigDecimal(row[6].toString()) : null)
                .gesamtEcFach(row[7] != null ? new BigDecimal(row[7].toString()) : null)
                .stundentyp((String) row[8])
                .datum(row[9] != null ? ((java.sql.Date) row[9]) : null)
                .uhrzeitVon(row[10] != null ? ((java.sql.Time) row[10]) : null)
                .uhrzeitBis(row[11] != null ? ((java.sql.Time) row[11]) : null)
                .pauseVon(row[12] != null ? ((java.sql.Time) row[12]) : null)
                .pauseBis(row[13] != null ? ((java.sql.Time) row[13]) : null)
                .inhalt((String) row[14])
                .bemerkung((String) row[15])
                .dauer(row[16] != null ? new BigDecimal(row[16].toString()) : null)
                .dauerPause(row[17] != null ? Long.valueOf(row[17].toString()) : null) // Updated for Long
                .kalenderwoche((Integer) row[18])
                .wochentag((Integer) row[19])
                .build();
    }


}

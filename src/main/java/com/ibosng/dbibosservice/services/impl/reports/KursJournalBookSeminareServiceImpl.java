package com.ibosng.dbibosservice.services.impl.reports;

import com.ibosng.dbibosservice.dtos.reports.KursJournalBookSeminareDto;
import com.ibosng.dbibosservice.services.reports.KursJournalBookSeminareService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class KursJournalBookSeminareServiceImpl implements KursJournalBookSeminareService {

    private final EntityManagerFactory entityManagerFactory;

    private static final String KURS_JOURNAL_BOOK_SEMINARE_QUERY = """
            select concat('P ',(select ASpnr from AUSSCHREIBUNG where ASnr = PROJEKT.ASnr)) as 'fuerdasProjekt',
                   PJbezeichnung1 as 'projektname',
                   SEMINAR.SMnr as 'seminar',
                   concat(PJdatumvon,' - ',PJdatumbis) as 'projektzeitraum',
                   CONCAT(LPAD(PJkstgr, 2, '0'), LPAD(PJkstnr, 2, '0'), LPAD(PJkostentraeger, 3, '0')) as 'kostentraeger',
                   (select concat(ADznf1 , ' ', ADvnf2) from ADRESSE where ADRESSE.ADadnr = PZLEISTUNG.ADadnr) as trainer,
                   SMADbezeichnung as 'fach',
                   case when LZtaetnr in('3333','4603','3831') then sum(LZdauer) end as 'gesamt_Seminar',
                   case when LZtaetnr in('4567','3339') then sum(LZdauer) end as 'gesamt_Fach_EC',
                   weekofyear(LZdatum) as 'kalenderwoche',
                   (weekofyear(LZdatum) - weekofyear(SEMINAR.SMdatumVon) + 1) as 'kurswoche'
            from PZLEISTUNG
            left join SEMINAR on SEMINAR.SMnr = PZLEISTUNG.LZSMnr
            inner join PROJEKT on PROJEKT.PJnr = SEMINAR.PJnr
            left join SM_AD on SM_AD.SMADnr = PZLEISTUNG.SM_AD_SMADnr
            left join PZLEISTUNG_SEMINARBUCH_ZUSATZ on pz_leistung_id = PZLEISTUNG.LZnr
            where (SEMINAR.SMnr = :sem1 OR SEMINAR.SMnr = :sem2 OR SEMINAR.SMnr = :sem3)
              and LZtyp = 'l'
              and LZtaetnr in('3333','4603','3831', '4567','3339')
            group by weekofyear(LZdatum);
            """;

    public KursJournalBookSeminareServiceImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<KursJournalBookSeminareDto> getKursJournalBooksByProjekt(int sem1, int sem2, int sem3) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            Query query = entityManager.createNativeQuery(KURS_JOURNAL_BOOK_SEMINARE_QUERY);

            // Set the parameters for the query
            query.setParameter("sem1", sem1);
            query.setParameter("sem2", sem2);
            query.setParameter("sem3", sem3);

            List<Object[]> results = query.getResultList();

            // Map the results to DTOs
            return results.stream()
                    .map(this::mapToKursJournalBookSeminareDto)
                    .toList();
        } finally {
            entityManager.close();
        }
    }


    private KursJournalBookSeminareDto mapToKursJournalBookSeminareDto(Object[] row) {
        return KursJournalBookSeminareDto.builder()
                .fuerdasProjekt((String) row[0])
                .projektname((String) row[1])
                .projektzeitraum((String) row[2])
                .kostentraeger((String) row[3])
                .trainer((String) row[4])
                .fach((String) row[5])
                .gesamt_Seminar(row[6] != null ? new BigDecimal(row[6].toString()) : null)
                .gesamt_Fach_EC(row[7] != null ? new BigDecimal(row[7].toString()) : null)
                .kalenderwoche((Integer) row[8])
                .kurswoche((Integer) row[9])
                .build();
    }

}

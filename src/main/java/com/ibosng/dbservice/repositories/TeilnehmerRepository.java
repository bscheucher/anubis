package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.lhr.AbmeldungStatus;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Repository
@Transactional("postgresTransactionManager")
public interface TeilnehmerRepository extends JpaRepository<Teilnehmer, Integer>, TeilnehmerRepositoryExtended, JpaSpecificationExecutor<Teilnehmer> {

    String GET_SUMMARY_IMPORTED_TEILNEHMER = """
            SELECT date_trunc('day', tn.changed_on)       AS date,
                               pr.projekt_nummer                      AS projektnummer,
                               s.seminar_nummer                       AS seminarNummer,
                               s.bezeichnung                          AS seminar,
                               t2s.massnahmennummer                   as massnahmennummer,
                               tn.import_filename,
                               COUNT(DISTINCT case
                                                  when tds.error <> 'seminar' or tds.teilnehmer is null
                                                      then tn.id end) AS gesamt, -- Counting distinct participants
                               COUNT(DISTINCT CASE
                                                  WHEN tn.status = 1 and tds.teilnehmer is null
                                                      THEN tn.id END) AS valid,  -- Counting distinct valid participants
                               COUNT(DISTINCT CASE
                                                  WHEN tn.status = 2 and tds.error <> 'seminar'
                                                      THEN tn.id END) AS invalid -- Counting distinct invalid participants
                        FROM teilnehmer tn
                                 JOIN
                             teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id and t2s.status = 1
                                 LEFT JOIN teilnehmer_data_status tds on tn.id = tds.teilnehmer-- and ds.data_status_value <> 25
                                 JOIN
                             seminar s ON t2s.seminar_id = s.id
                                 JOIN
                             projekt pr on pr.id = s.project
                        WHERE (tn.changed_on >= :changedDate
                           OR tn.created_on >= :changedDate)
                        and tn.import_filename not like '%Service%'
                        GROUP BY date, s.seminar_nummer, s.bezeichnung, pr.projekt_nummer, tn.import_filename, t2s.status, t2s.massnahmennummer
                        HAVING COUNT(DISTINCT CASE WHEN tds.error <> 'seminar' OR tds.teilnehmer IS NULL THEN tn.id END) > 0
                            OR COUNT(DISTINCT CASE WHEN tn.status = 1 AND tds.teilnehmer IS NULL THEN tn.id END) > 0
                            OR COUNT(DISTINCT CASE WHEN tn.status = 2 AND tds.error <> 'seminar' THEN tn.id END) > 0
                        UNION
                        SELECT date_trunc('day', tn.changed_on) AS date,
                               pr.projekt_nummer                AS projektnummer,
                               s.seminar_nummer                 AS seminarNummer,
                               s.bezeichnung                    AS seminar,
                               t2s.massnahmennummer,
                               tn.import_filename,
                               COUNT(DISTINCT CASE
                                                  WHEN EXISTS (SELECT 1
                                                               FROM teilnehmer_data_status tds_sub
                                                               WHERE tds_sub.teilnehmer = tn.id
                                                                 AND tds_sub.error = 'seminar')
                                                      OR tds.teilnehmer IS NULL
                                                      THEN tn.id
                                   END)                         AS gesamt, -- Counting distinct participants
                               COUNT(DISTINCT CASE
                                                  WHEN tn.status = 1
                                                      AND tds.teilnehmer IS NULL
                                                      THEN tn.id
                                   END)                         AS valid,  -- Counting distinct valid participants
                               COUNT(DISTINCT CASE
                                                  WHEN tn.status = 2
                                                      AND EXISTS (SELECT 1
                                                                  FROM teilnehmer_data_status tds_sub
                                                                  WHERE tds_sub.teilnehmer = tn.id
                                                                    AND tds_sub.error = 'seminar')
                                                      THEN tn.id
                                   END)                         AS invalid -- Counting distinct invalid participants
                        FROM teilnehmer tn
                                 LEFT JOIN
                             teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id AND t2s.status = 2
                                 LEFT JOIN
                             teilnehmer_data_status tds ON tn.id = tds.teilnehmer
                                 LEFT JOIN
                             seminar s ON t2s.seminar_id = s.id
                                 LEFT JOIN
                             projekt pr ON pr.id = s.project
                        WHERE (tn.changed_on >= :changedDate
                            OR tn.created_on >= :changedDate)
                          AND tn.status = 2
                        and tn.import_filename not like '%Service%'
                        GROUP BY date, s.seminar_nummer, s.bezeichnung, pr.projekt_nummer, tn.import_filename, t2s.status, t2s.massnahmennummer
                        HAVING COUNT(DISTINCT CASE
                                                  WHEN EXISTS (SELECT 1
                                                               FROM teilnehmer_data_status tds_sub
                                                               WHERE tds_sub.teilnehmer = tn.id
                                                                 AND tds_sub.error = 'seminar')
                                                      OR tds.teilnehmer IS NULL
                                                      THEN tn.id
                            END) > 0
                        ORDER BY date, seminar;""";

    String GET_SEMINARS_FOR_TN_BY_STATUS = """
            select distinct sem.bezeichnung from teilnehmer tn
                    join teilnehmer_2_seminar t2s on tn.id = t2s.teilnehmer_id
                    join seminar sem on t2s.seminar_id = sem.id
                    join projekt pr on pr.id = sem.project
                    where tn.status = :status
            """;

    String GET_TEILNEHMER_FOR_STATUS_AND_SEMINAR = """
            SELECT
                distinct (tn.*)
            FROM
                teilnehmer tn
            join teilnehmer_data_status tds on tds.teilnehmer = tn.id and tds.error <> 'seminar'
            JOIN
                teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id and t2s.status = 1
            JOIN
                seminar sem ON t2s.seminar_id = sem.id AND sem.bezeichnung = :seminar
            JOIN
                projekt pr ON pr.id = sem.project
            WHERE
                tn.status = :status
                order by tn.nachname
            LIMIT :size OFFSET :offset""";

    String GET_COUNT_TEILNEHMER_FOR_STATUS_AND_SEMINAR = """
            SELECT
                count(distinct (tn.*))
            FROM
                teilnehmer tn
            join teilnehmer_data_status tds on tds.teilnehmer = tn.id and tds.error <> 'seminar'
            JOIN
                teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id and t2s.status = 1
            JOIN
                seminar sem ON t2s.seminar_id = sem.id AND sem.bezeichnung = :seminar
            JOIN
                projekt pr ON pr.id = sem.project
            WHERE
                tn.status = :status""";

    String GET_TEILNEHMER_FOR_STATUS_AND_MASSNAHMENUMMER = """
            SELECT distinct(tn.*) FROM teilnehmer tn
              JOIN teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id AND t2s.massnahmennummer = :massnahmenummer
            WHERE
                tn.status = :status
                order by tn.nachname
              LIMIT :size OFFSET :offset
            """;

    String GET_COUNT_TEILNEHMER_FOR_STATUS_AND_MASSNAHMENUMMER = """
            SELECT count(distinct (tn.*)) FROM teilnehmer tn
              JOIN teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id AND t2s.massnahmennummer = :massnahmenummer
            WHERE
                tn.status = :status
            """;

    String GET_MASSNAHMENUMMERS_FOR_TN_BY_STATUS = """
            select distinct t2s.massnahmennummer from teilnehmer tn
                join teilnehmer_staging ts on tn.id = ts.teilnehmer_id and tn.import_filename=ts.import_filename and tn.status = :status
                join teilnehmer_2_seminar t2s on tn.id = t2s.teilnehmer_id
                where t2s.massnahmennummer is not null;
            """;

    String FIND_TEILNEHMER_FILTERED_TR = """
            SELECT DISTINCT tn.id as id,
                                   tn.vorname as vorname,
                                   tn.nachname as nachname,
                                   STRING_AGG(DISTINCT t2s.massnahmennummer, ',' ORDER BY t2s.massnahmennummer) AS massnahmennummer,
                                   STRING_AGG(DISTINCT sem.bezeichnung, ',' ORDER BY sem.bezeichnung) AS seminarNamen,
                                   tn.sv_nummer as svn,
                                   la.land_name as land,
                                   ad.ort as ort,
                                   pl.plz,
                                   STRING_AGG(DISTINCT sem.bezeichnung, ', ' ORDER BY sem.bezeichnung) AS angemeldetIn,
                                   tn.is_ueba as isUeba,
                                   tn.status,
                                   STRING_AGG(DISTINCT tds.error, ',' ORDER BY tds.error) AS errors
                        FROM teilnehmer tn
                        LEFT JOIN teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id
                        LEFT JOIN seminar sem ON t2s.seminar_id = sem.id
                        LEFT JOIN projekt pr ON sem.project = pr.id
                        LEFT JOIN seminar_2_trainer s2t ON s2t.seminar_id = sem.id
                        LEFT JOIN benutzer b ON s2t.trainer_id = b.id
                        LEFT JOIN geschlecht g ON tn.geschlecht = g.id
                        LEFT JOIN adresse ad on tn.adresse = ad.id
                        LEFT JOIN land la on ad.land = la.id
                        LEFT JOIN plz pl on ad.plz = pl.id
                        LEFT JOIN teilnehmer_data_status tds on tn.id = tds.teilnehmer
                        WHERE
                          (
                            :identifiersString IS NULL OR
                            (
                              -- SV-Nummer
                              (:identifiersString ~ '^\\d{10}$' AND tn.sv_nummer = :identifiersString)
                              OR
                              -- Name search
                              (
                                (:identifiersString !~ '^\\d{10}$') AND
                                (
                                  LOWER(tn.vorname) LIKE CONCAT('%', LOWER(:identifiersString), '%')
                                  OR LOWER(tn.nachname) LIKE CONCAT('%', LOWER(:identifiersString), '%')
                                )
                              )
                            )
                          )
                          AND (:seminarName IS NULL OR sem.bezeichnung = :seminarName)
                          AND (:projektName IS NULL OR pr.bezeichnung = :projektName)
                          AND (:isUebaTeilnehmer IS NULL OR tn.is_ueba = :isUebaTeilnehmer)
                          AND (:geschlecht IS NULL OR LOWER(g.name) = LOWER(:geschlecht))
                          AND (
                            :isAngemeldet IS NULL OR
                            (
                              :isAngemeldet = TRUE
                              AND sem.end_date >= CURRENT_DATE
                              AND sem.status = 1
                            )
                          )
                          AND (:benutzerId IS NULL OR b.id = :benutzerId)
                          AND (
                            :isActive IS NULL OR
                            (
                              sem.end_date >= CURRENT_DATE
                              AND sem.status = 1
                              AND pr.end_date >= CURRENT_DATE
                            )
                          )
                          AND (:massnahmennummer IS NULL OR t2s.massnahmennummer = :massnahmennummer)
                          AND (
                            (:isFehlerhaft IS TRUE AND tn.status = 2) OR
                            ((:isFehlerhaft IS FALSE OR :isFehlerhaft IS NULL) AND tn.status = 1)
                          )
                        GROUP BY tn.id, tn.vorname, tn.nachname, tn.sv_nummer, la.land_name, ad.ort, pl.plz, tn.is_ueba, tn.status""";

    String FIND_TEILNEHMER_FILTERED_PR = """
            SELECT DISTINCT tn.id as id,
                                   tn.vorname as vorname,
                                   tn.nachname as nachname,
                                   STRING_AGG(DISTINCT t2s.massnahmennummer, ',' ORDER BY t2s.massnahmennummer) AS massnahmennummer,
                                   STRING_AGG(DISTINCT sem.bezeichnung, ',' ORDER BY sem.bezeichnung) AS seminarNamen,
                                   tn.sv_nummer as svn,
                                   la.land_name as land,
                                   ad.ort as ort,
                                   pl.plz,
                                   STRING_AGG(DISTINCT sem.bezeichnung, ', ' ORDER BY sem.bezeichnung) AS angemeldetIn,
                                   tn.is_ueba as isUeba,
                                   tn.status,
                                   STRING_AGG(DISTINCT tds.error, ',' ORDER BY tds.error) AS errors
                        FROM teilnehmer tn
                        LEFT JOIN teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id
                        LEFT JOIN seminar sem ON t2s.seminar_id = sem.id
                        LEFT JOIN projekt pr ON sem.project = pr.id
                        LEFT JOIN projekt_2_manager p2m on pr.id = p2m.projekt
                        LEFT JOIN benutzer pm ON p2m.manager = pm.id
                        LEFT JOIN geschlecht g ON tn.geschlecht = g.id
                        LEFT JOIN adresse ad on tn.adresse = ad.id
                        LEFT JOIN land la on ad.land = la.id
                        LEFT JOIN plz pl on ad.plz = pl.id
                        LEFT JOIN teilnehmer_data_status tds on tn.id = tds.teilnehmer
                        WHERE
                          (
                            :identifiersString IS NULL OR
                            (
                              -- SV-Nummer
                              (:identifiersString ~ '^\\d{10}$' AND tn.sv_nummer = :identifiersString)
                              OR
                              -- Name search
                              (
                                (:identifiersString !~ '^\\d{10}$') AND
                                (
                                  LOWER(tn.vorname) LIKE CONCAT('%', LOWER(:identifiersString), '%')
                                  OR LOWER(tn.nachname) LIKE CONCAT('%', LOWER(:identifiersString), '%')
                                )
                              )
                            )
                          )
                          AND (:seminarName IS NULL OR sem.bezeichnung = :seminarName)
                          AND (:projektName IS NULL OR pr.bezeichnung = :projektName)
                          AND (:isUebaTeilnehmer IS NULL OR tn.is_ueba = :isUebaTeilnehmer)
                          AND (:geschlecht IS NULL OR LOWER(g.name) = LOWER(:geschlecht))
                          AND (
                            :isAngemeldet IS NULL OR
                            (
                              :isAngemeldet = TRUE
                              AND sem.end_date >= CURRENT_DATE
                              AND sem.status = 1
                            )
                          )
                          AND (:benutzerId IS NULL OR pm.id = :benutzerId)
                          AND (
                            :isActive IS NULL OR
                            (
                              sem.end_date >= CURRENT_DATE
                              AND sem.status = 1
                              AND pr.end_date >= CURRENT_DATE
                            )
                          )
                          AND (:massnahmennummer IS NULL OR t2s.massnahmennummer = :massnahmennummer)
                          AND (
                            (:isFehlerhaft IS TRUE AND tn.status = 2) OR
                            ((:isFehlerhaft IS FALSE OR :isFehlerhaft IS NULL) AND tn.status = 1)
                          )
                        GROUP BY tn.id, tn.vorname, tn.nachname, tn.sv_nummer, la.land_name, ad.ort, pl.plz, tn.is_ueba, tn.status""";

    String FIND_TEILNEHMER_FILTERED = "SELECT * FROM (" + FIND_TEILNEHMER_FILTERED_TR + " UNION " + FIND_TEILNEHMER_FILTERED_PR + " ) tn";

    String FIND_TEILNEHMER_FILTERED_VORNAME_ASC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.vorname ASC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_VORNAME_DESC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.vorname DESC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_NACHNAME_ASC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.nachname ASC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_NACHNAME_DESC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.nachname DESC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_SVNR_ASC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.svn ASC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_SVNR_DESC = FIND_TEILNEHMER_FILTERED + " ORDER BY tn.svn DESC\n" +
            "LIMIT :size OFFSET :offset";

    String FIND_TEILNEHMER_FILTERED_COUNT = "SELECT COUNT(*) FROM (" + FIND_TEILNEHMER_FILTERED_TR + " UNION " + FIND_TEILNEHMER_FILTERED_PR + " ) tn";

    List<Teilnehmer> findAllByImportFilename(String importFilename);

    List<Teilnehmer> findBySvNummer(String svn);

    List<Teilnehmer> findByVorname(String vorname);

    List<Teilnehmer> findByNachname(String nachname);

    List<Teilnehmer> findByVornameAndNachname(String vorname, String nachname);

    @Query(value = GET_SUMMARY_IMPORTED_TEILNEHMER, nativeQuery = true)
    List<Object[]> getSummaryImportedTeilnehmer(LocalDate changedDate);

    @Query(value = GET_SEMINARS_FOR_TN_BY_STATUS, nativeQuery = true)
    List<String> getSeminarsForTeilnehmerWithStatus(TeilnehmerStatus status);

    @Query(value = GET_TEILNEHMER_FOR_STATUS_AND_SEMINAR, nativeQuery = true)
    List<Teilnehmer> getTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar, int size, int offset);

    @Query(value = GET_COUNT_TEILNEHMER_FOR_STATUS_AND_SEMINAR, nativeQuery = true)
    Integer getCountTeilnehmerWithStatusAndSeminar(TeilnehmerStatus status, String seminar);

    @Query(value = GET_MASSNAHMENUMMERS_FOR_TN_BY_STATUS, nativeQuery = true)
    List<String> getMassnahmenummersForTeilnehmerWithStatus(TeilnehmerStatus status);

    @Query(value = GET_TEILNEHMER_FOR_STATUS_AND_MASSNAHMENUMMER, nativeQuery = true)
    List<Teilnehmer> getTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer, int size, int offset);

    @Query(value = GET_COUNT_TEILNEHMER_FOR_STATUS_AND_MASSNAHMENUMMER, nativeQuery = true)
    Integer getCountTeilnehmerWithStatusAndMassnahmenummer(TeilnehmerStatus status, String massnahmenummer);

    Page<Teilnehmer> findByStatus(TeilnehmerStatus status, Pageable pageable);

    List<Teilnehmer> findAllByCreatedOnAfterOrChangedOnAfter(LocalDateTime createdOn, LocalDateTime changedOn);

    @Query("select t from Teilnehmer t where t.personalnummer.personalnummer = :personalnummer")
    Teilnehmer findByPersonalnummerString(String personalnummer);

    Teilnehmer findByPersonalnummer_Id(Integer personalnummer);

    @Modifying
    @Query("UPDATE Teilnehmer t SET t.hasBisDocument = :hasBisDocument WHERE t.id = :id")
    void updateTeilnehmerHasBisDocument(@Param("hasBisDocument") boolean hasBisDocument, @Param("id") Integer id);

    List<Teilnehmer> findAllByVornameAndNachnameAndSvNummer(String vorname, String nachname, String svNummer);

    List<Teilnehmer> findAllByVornameAndNachnameAndGeburtsdatum(String vorname, String nachname, LocalDate geburtsdatum);

    @Query("SELECT t FROM Teilnehmer t JOIN Abmeldung abm ON abm.personalnummer.id = t.personalnummer.id and abm.status = :status")
    List<Teilnehmer> findAllByAbgemeldetenTeilnehmer(AbmeldungStatus status);

    List<Teilnehmer> findByIdIn(Collection<Integer> ids);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_VORNAME_ASC)
    List<Object[]> findTeilnehmerFilteredNativeVornameAsc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_VORNAME_DESC)
    List<Object[]> findTeilnehmerFilteredNativeVornameDesc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_NACHNAME_ASC)
    List<Object[]> findTeilnehmerFilteredNativeNachnameAsc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_NACHNAME_DESC)
    List<Object[]> findTeilnehmerFilteredNativeNachnameDesc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_SVNR_ASC)
    List<Object[]> findTeilnehmerFilteredNativeSvnrAsc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_SVNR_DESC)
    List<Object[]> findTeilnehmerFilteredNativeSvnrDesc(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId, int size, int offset);

    @Query(nativeQuery = true, value = FIND_TEILNEHMER_FILTERED_COUNT)
    long findTeilnehmerFilteredCount(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Integer benutzerId);

}

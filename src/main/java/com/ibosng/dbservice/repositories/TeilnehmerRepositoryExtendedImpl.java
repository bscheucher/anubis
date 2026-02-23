package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.masterdata.Geschlecht;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer2Seminar;
import com.ibosng.dbservice.entities.teilnehmer.TeilnehmerStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
public class TeilnehmerRepositoryExtendedImpl implements TeilnehmerRepositoryExtended {
    private final EntityManagerFactory entityManagerFactory;

    public TeilnehmerRepositoryExtendedImpl(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Teilnehmer> findTeilnehmerFiltered1(String identifiersString,
                                                    String seminarName,
                                                    String projektName,
                                                    Boolean isActive,
                                                    Boolean isUebaTeilnehmer,
                                                    Boolean isAngemeldet,
                                                    String geschlecht,
                                                    Boolean isFehlerhaft,
                                                    String massnahmennummer,
                                                    Integer benutzerId,
                                                    String sortProperty,
                                                    String direction,
                                                    int page,
                                                    int size) {


        String sortColumn = validateSortProperty(sortProperty);
        String sortDirection = "ASC".equalsIgnoreCase(direction) ? "ASC" : "DESC";

        String sql = String.format("""
                    SELECT DISTINCT tn.*
                    FROM teilnehmer tn
                    LEFT JOIN teilnehmer_2_seminar t2s ON tn.id = t2s.teilnehmer_id
                    LEFT JOIN seminar sem ON t2s.seminar_id = sem.id
                    LEFT JOIN projekt pr ON sem.project = pr.id
                    LEFT JOIN seminar_2_trainer s2t ON s2t.seminar_id = sem.id
                    LEFT JOIN benutzer b ON s2t.trainer_id = b.id
                    LEFT JOIN geschlecht g ON tn.geschlecht = g.id
                    WHERE
                      (
                        COALESCE(:identifiersString, '') = '' OR
                        (
                          (COALESCE(:identifiersString, '') ~ '^\\d{10}$' AND tn.sv_nummer = :identifiersString)
                          OR
                          (COALESCE(:identifiersString, '') !~ '^\\d{10}$' AND (
                              LOWER(tn.vorname) LIKE CONCAT('%%', LOWER(:identifiersString), '%%')
                              OR LOWER(tn.nachname) LIKE CONCAT('%%', LOWER(:identifiersString), '%%')
                          ))
                        )
                      )
                      AND (COALESCE(:seminarName, '') = '' OR sem.bezeichnung = :seminarName)
                      AND (COALESCE(:projektName, '') = '' OR pr.bezeichnung = :projektName)
                      AND (CAST(:isUebaTeilnehmer AS BOOLEAN) IS NULL OR tn.is_ueba = :isUebaTeilnehmer)
                      AND (COALESCE(:geschlecht, '') = '' OR LOWER(g.name) = LOWER(:geschlecht))
                      AND (
                        CAST(:isAngemeldet AS BOOLEAN) IS NULL OR
                        (
                          CAST(:isAngemeldet AS BOOLEAN) = TRUE
                          AND sem.end_date >= CURRENT_DATE
                          AND sem.status = 1
                        )
                      )
                      AND (CAST(:benutzerId AS INTEGER) IS NULL OR b.id = :benutzerId)
                      AND (
                        CAST(:isActive AS BOOLEAN) IS NULL OR
                        (
                          sem.end_date >= CURRENT_DATE
                          AND sem.status = 1
                          AND pr.end_date >= CURRENT_DATE
                        )
                      )
                      AND (COALESCE(:massnahmennummer, '') = '' OR t2s.massnahmennummer = :massnahmennummer)
                      AND (CAST(:isFehlerhaft AS BOOLEAN) IS NULL OR tn.status = 2)
                    ORDER BY %s %s
                    LIMIT :limit OFFSET :offset
                """, sortColumn, sortDirection);

        EntityManager entityManager = entityManagerFactory.createEntityManager();

        Query query = entityManager.createNativeQuery(sql, Teilnehmer.class);
        // Set parameters
        query.setParameter("identifiersString", identifiersString);
        query.setParameter("seminarName", seminarName);
        query.setParameter("projektName", projektName);

        query.setParameter("isUebaTeilnehmer", isUebaTeilnehmer);
        query.setParameter("geschlecht", geschlecht);
        query.setParameter("isAngemeldet", isAngemeldet);
        query.setParameter("benutzerId", benutzerId);
        query.setParameter("isActive", isActive);
        query.setParameter("massnahmennummer", massnahmennummer);
        query.setParameter("isFehlerhaft", isFehlerhaft);


        query.setParameter("limit", size);
        query.setParameter("offset", page * size);


        return (List<Teilnehmer>) query.getResultList();
    }

    private String validateSortProperty(String input) {
        return switch (input) {
            case "vorname" -> "tn.vorname";
            case "nachname" -> "tn.nachname";
            case "geburtsdatum" -> "tn.geburtsdatum";
            case "id" -> "tn.id";
            default -> "tn.id"; // default fallback
        };
    }

    @Override
    public Page<Teilnehmer> findTeilnehmerFiltered(String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Benutzer benutzer, String sortProperty, Sort.Direction direction, int page, int size) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            size = Math.max(size, 1);
            int offset = page * size;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperty));
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            List<Teilnehmer> resultList;
            // Main query
            CriteriaQuery<Teilnehmer> query = cb.createQuery(Teilnehmer.class);
            Root<Teilnehmer> teilnehmerRoot = query.from(Teilnehmer.class);
            resultList = generalFilter(entityManager,
                    teilnehmerRoot,
                    identifiersString,
                    seminarName,
                    projektName,
                    isActive,
                    isUebaTeilnehmer,
                    isAngemeldet,
                    geschlecht,
                    isFehlerhaft,
                    massnahmennummer,
                    benutzer,
                    cb,
                    query,
                    pageable,
                    offset,
                    size);


            long totalElements = getTotalCount(entityManager, cb, identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzer);
            return new PageImpl<>(resultList, pageable, totalElements);
        } catch (Exception ex) {
            log.error("Exception caught while trying to get sorted and ordered TN: {}", ex.getMessage());
        } finally {
            entityManager.close();
        }
        return null;
    }

    private List<Teilnehmer> generalFilter(EntityManager entityManager,
                                           Root<Teilnehmer> teilnehmerRoot,
                                           String identifiersString,
                                           String seminarName,
                                           String projektName,
                                           Boolean isActive,
                                           Boolean isUebaTeilnehmer,
                                           Boolean isAngemeldet,
                                           String geschlecht,
                                           Boolean isFehlerhaft,
                                           String massnahmennummer,
                                           Benutzer benutzer,
                                           CriteriaBuilder cb,
                                           CriteriaQuery<Teilnehmer> query,
                                           Pageable pageable,
                                           int offset,
                                           int size) {
        Join<Teilnehmer, Teilnehmer2Seminar> joinTeilnehmerSeminar = teilnehmerRoot.join("teilnehmerSeminars", JoinType.LEFT);
        Join<Teilnehmer2Seminar, Seminar> joinSeminar = joinTeilnehmerSeminar.join("seminar", JoinType.LEFT);
        Join<Seminar, Projekt> joinProject = joinSeminar.join("project", JoinType.LEFT);
        Join<Seminar2Trainer, Seminar> joinTrainerSeminar = joinSeminar.join("trainerSeminars", JoinType.LEFT);
        Join<Seminar2Trainer, Benutzer> joinBenutzer = joinTrainerSeminar.join("trainer", JoinType.LEFT);
        List<Predicate> predicates = buildGeneralPredicates(identifiersString, seminarName, projektName, isActive, isUebaTeilnehmer, isAngemeldet, geschlecht, isFehlerhaft, massnahmennummer, benutzer, cb, teilnehmerRoot, joinTeilnehmerSeminar, joinSeminar, joinBenutzer, joinProject);
        query.select(teilnehmerRoot).distinct(true) // Adding DISTINCT here
                .where(cb.and(predicates.toArray(new Predicate[0])));

        // Apply sorting
        applySorting(pageable, cb, query, teilnehmerRoot);

        // Create query and set pagination
        Query jpaQuery = entityManager.createQuery(query);
        jpaQuery.setFirstResult(offset);
        jpaQuery.setMaxResults(size);

        // Get results
        return jpaQuery.getResultList();
    }

    private List<Predicate> buildGeneralPredicates(String identifiersString,
                                                   String seminarName,
                                                   String projektName,
                                                   Boolean isActive,
                                                   Boolean isUebaTeilnehmer,
                                                   Boolean isAngemeldet,
                                                   String geschlecht,
                                                   Boolean isFehlerhaft,
                                                   String massnahmennummer,
                                                   Benutzer benutzer,
                                                   CriteriaBuilder cb,
                                                   Root<Teilnehmer> teilnehmerRoot,
                                                   Join<Teilnehmer, Teilnehmer2Seminar> joinTeilnehmerSeminar,
                                                   Join<Teilnehmer2Seminar, Seminar> joinSeminar,
                                                   Join<Seminar2Trainer, Benutzer> joinTrainer,
                                                   Join<Seminar, Projekt> joinProject) {
        List<Predicate> predicates = new ArrayList<>();

        if (!isNullOrBlank(identifiersString)) {
            List<Predicate> namePredicates = new ArrayList<>();
            if (identifiersString.matches("\\d{10}")) {
                predicates.add(cb.equal(teilnehmerRoot.get("svNummer"), identifiersString));
            } else {
                String[] identifiers = identifiersString.split("\\s+");
                // Predicates for vorname and nachname
                for (String identifier : identifiers) {
                    String lowerIdentifier = "%" + identifier.toLowerCase() + "%";
                    // VORNAME
                    namePredicates.add(cb.like(cb.lower(teilnehmerRoot.get("vorname")), lowerIdentifier));
                    // NACHNAME
                    namePredicates.add(cb.like(cb.lower(teilnehmerRoot.get("nachname")), lowerIdentifier));
                }
                // Combine name predicates with OR
                Predicate namePredicate = cb.or(namePredicates.toArray(new Predicate[0]));
                predicates.add(namePredicate);
            }
        }

        // Predicate for seminarName
        if (!isNullOrBlank(seminarName)) {
            predicates.add(cb.equal(joinSeminar.get("bezeichnung"), seminarName));
        }

        // Predicate for projektName
        if (!isNullOrBlank(projektName)) {
            predicates.add(cb.equal(joinProject.get("bezeichnung"), projektName));
        }

        //Predicate for isUeba
        if (isUebaTeilnehmer != null) {
            predicates.add(cb.equal(teilnehmerRoot.get("isUeba"), isUebaTeilnehmer));
        }

        //Predicate for geschlecht
        if (!isNullOrBlank(geschlecht)) {
            Join<Teilnehmer, Geschlecht> joinGeschlect = teilnehmerRoot.join("geschlecht", JoinType.LEFT);
            predicates.add(cb.equal(cb.lower(joinGeschlect.get("name")), geschlecht.toLowerCase()));
        }

        //Predicate for active seminars
        if (isAngemeldet != null && isAngemeldet) {
            predicates.add(cb.greaterThanOrEqualTo(joinSeminar.get("endDate"), LocalDate.now()));
            predicates.add(cb.equal(joinSeminar.get("status"), TeilnehmerStatus.VALID.getCode()));
        }

        if (benutzer != null) {
            predicates.add(cb.equal(joinTrainer.get("id"), benutzer.getId()));
        }

        //Predicate for active seminars and projects
        if (isActive != null) {
            TeilnehmerStatus status = isActive ? TeilnehmerStatus.VALID : TeilnehmerStatus.INVALID;
            predicates.add(cb.greaterThanOrEqualTo(joinSeminar.get("endDate"), LocalDate.now()));
            predicates.add(cb.equal(joinSeminar.get("status"), status.getCode()));
            predicates.add(cb.greaterThanOrEqualTo(joinProject.get("endDate"), LocalDate.now()));
        }

        //Predicate for massnahmennummer
        if (!isNullOrBlank(massnahmennummer)) {
            predicates.add(cb.equal(joinTeilnehmerSeminar.get("massnahmennummer"), massnahmennummer));
        }

        //Predicate for valid or invalid TN
        if (isFehlerhaft != null) {
            TeilnehmerStatus status = isFehlerhaft ? TeilnehmerStatus.INVALID : TeilnehmerStatus.VALID;
            predicates.add(cb.equal(teilnehmerRoot.get("status"), status.getCode()));
        }

        return predicates;
    }

    private void applySorting(Pageable pageable, CriteriaBuilder cb, CriteriaQuery<Teilnehmer> query, Root<Teilnehmer> teilnehmerRoot) {
        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                Order jpaOrder = order.isAscending() ? cb.asc(teilnehmerRoot.get(order.getProperty())) : cb.desc(teilnehmerRoot.get(order.getProperty()));
                orders.add(jpaOrder);
            });
            query.orderBy(orders);
        }
    }


    private long getTotalCount(EntityManager entityManager, CriteriaBuilder cb, String identifiersString, String seminarName, String projektName, Boolean isActive, Boolean isUebaTeilnehmer, Boolean isAngemeldet, String geschlecht, Boolean isFehlerhaft, String massnahmennummer, Benutzer benutzer) {
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Teilnehmer> countRoot = countQuery.from(Teilnehmer.class);

        List<Predicate> countPredicates;

        // Apply general filter logic
        Join<Teilnehmer, Teilnehmer2Seminar> countJoinTeilnehmerSeminar = countRoot.join("teilnehmerSeminars", JoinType.LEFT);
        Join<Teilnehmer2Seminar, Seminar> countJoinSeminar = countJoinTeilnehmerSeminar.join("seminar", JoinType.LEFT);
        Join<Seminar, Projekt> countJoinProject = countJoinSeminar.join("project", JoinType.LEFT);
        Join<Seminar2Trainer, Seminar> joinTrainerSeminar = countJoinSeminar.join("trainerSeminars", JoinType.LEFT);
        Join<Seminar2Trainer, Benutzer> joinBenutzer = joinTrainerSeminar.join("trainer", JoinType.LEFT);

        countPredicates = buildGeneralPredicates(
                identifiersString,
                seminarName,
                projektName,
                isActive,
                isUebaTeilnehmer,
                isAngemeldet,
                geschlecht,
                isFehlerhaft,
                massnahmennummer,
                benutzer,
                cb,
                countRoot,
                countJoinTeilnehmerSeminar,
                countJoinSeminar,
                joinBenutzer,
                countJoinProject);

        countQuery.select(cb.countDistinct(countRoot))
                .where(cb.and(countPredicates.toArray(new Predicate[0])));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

}

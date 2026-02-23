package com.ibosng.dbservice.repositories.seminar;

import com.ibosng.dbservice.dtos.SeminarAnAbwesenheitDto;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.Projekt;
import com.ibosng.dbservice.entities.Status;
import com.ibosng.dbservice.entities.mitarbeiter.Seminar2Trainer;
import com.ibosng.dbservice.entities.seminar.Seminar;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ibosng.dbservice.utils.Parsers.getLocalDateNow;
import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
@Repository("seminarRepositoryExtendedImpl")
public class SeminarRepositoryExtendedImpl implements SeminarRepositoryExtended {
    private final EntityManagerFactory entityManagerFactory;

    public SeminarRepositoryExtendedImpl(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Seminar> findByBezeichnungIsActiveAndBenutzer(String projektBezeichnung, Boolean isActive, Benutzer benutzer) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Seminar> query = cb.createQuery(Seminar.class);
            Root<Seminar> seminar = query.from(Seminar.class);
            Join<Seminar, Projekt> joinProjekt = seminar.join("project", JoinType.LEFT);
            Join<Seminar2Trainer, Seminar> joinTrainerSeminar = seminar.join("trainerSeminars", JoinType.LEFT);
            Join<Seminar2Trainer, Benutzer> joinBenutzer = joinTrainerSeminar.join("trainer", JoinType.LEFT);

            List<Predicate> predicates = new ArrayList<>();

            if (!isNullOrBlank(projektBezeichnung)) {
                predicates.add(cb.equal(cb.lower(joinProjekt.get("bezeichnung")), projektBezeichnung.toLowerCase()));
            }

            if (isActive != null) {
                predicates.add(cb.equal(seminar.get("status"), isActive ? Status.ACTIVE : Status.INACTIVE));
                if (isActive) {
                    predicates.add(cb.between(cb.literal(getLocalDateNow().toLocalDate()), seminar.get("startDate"), seminar.get("endDate")));
                } else {
                    predicates.add(cb.lessThan(seminar.get("startDate"), getLocalDateNow().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
                    predicates.add(cb.greaterThan(seminar.get("endDate"), getLocalDateNow().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE)));
                }
            }

            if (benutzer != null) {
                predicates.add(cb.equal(joinBenutzer.get("id"), benutzer.getId()));
            }
            query.select(seminar)
                    .where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

            return entityManager.createQuery(query)
                    .getResultList();
        }
    }

    @Override
    public List<Seminar> findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam) {
        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Seminar> query = cb.createQuery(Seminar.class);
            Root<Seminar> Seminar = query.from(Seminar.class);

            Path<String> sMbezeichnung1 = Seminar.get("bezeichnung");
            Path<String> datumVon = Seminar.get("startDate");
            Path<String> datumBis = Seminar.get("endDate");
            Path<String> zeitVon = Seminar.get("startTime");

            List<Predicate> predicates = new ArrayList<>();
            for (String identifier : identifiers) {
                predicates.add(cb.like(sMbezeichnung1, "%" + identifier + "%"));
            }
            if (datumVonParam != null) {
                predicates.add(cb.equal(datumVon, datumVonParam));
            }
            if (datumBisParam != null) {
                predicates.add(cb.equal(datumBis, datumBisParam));
            }
            if (zeitVonParam != null) {
                predicates.add(cb.equal(zeitVon, zeitVonParam));
            }
            query.select(Seminar)
                    .where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

            return entityManager.createQuery(query)
                    .getResultList();
        }
    }

    @Override
    public Page<SeminarAnAbwesenheitDto> findSeminarFiltered(
            boolean isAdmin, Integer benutzerId, Boolean isActive, String projectName, String seminarName,
            LocalDate kursEndeSpaeterAls, LocalDate kursEndeFrueherAls,
            Boolean verzoegerung, String sortProperty, Direction sortDirection, int page, int size) {

        try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SeminarAnAbwesenheitDto> cq = cb.createQuery(SeminarAnAbwesenheitDto.class);
            Root<Seminar> seminarRoot = cq.from(Seminar.class);
            Join<Seminar, Projekt> projectJoin = seminarRoot.join("project", JoinType.INNER);
            // Calculate AGE first
            Expression<Long> ageExpression = cb.function("AGE", Long.class, cb.currentDate(), seminarRoot.get("endDate"));

            // Extract the number of days
            Expression<Double> delayExpression = cb.function("DATE_PART", Double.class, cb.literal("day"), ageExpression);
            cq.select(cb.construct(
                    SeminarAnAbwesenheitDto.class,
                    seminarRoot.get("id").alias("seminarId"),
                    seminarRoot.get("bezeichnung").alias("seminar"),
                    projectJoin.get("bezeichnung").alias("project"),
                    seminarRoot.get("standort").alias("standort"),
                    seminarRoot.get("startDate").alias("von"),
                    seminarRoot.get("endDate").alias("bis"),
                    delayExpression.alias("verzoegerung"),
                    seminarRoot.get("changedOn").alias("changedOn")
            ));

            List<Predicate> predicates = buildPredicates(cb, seminarRoot, projectJoin, isAdmin, benutzerId,
                    isActive, projectName, seminarName, kursEndeSpaeterAls, kursEndeFrueherAls, verzoegerung, cq);

            if (!predicates.isEmpty()) {
                cq.where(cb.and(predicates.toArray(new Predicate[0])));
            }

            Expression<?> orderByExpression = switch (sortProperty.toLowerCase()) {
                case "project" -> projectJoin.get("bezeichnung");
                case "standort" -> seminarRoot.get("standort");
                case "von" -> seminarRoot.get("startDate");
                case "bis" -> seminarRoot.get("endDate");
                case "verzoegerung" -> delayExpression;
                case "changedon" -> seminarRoot.get("changedOn");
                default -> seminarRoot.get("bezeichnung");
            };

            cq.orderBy(sortDirection != Direction.ASC ?
                    cb.desc(orderByExpression) :
                    cb.asc(orderByExpression));

            TypedQuery<SeminarAnAbwesenheitDto> pagedQuery = entityManager.createQuery(cq);
            pagedQuery.setFirstResult(page * size);
            pagedQuery.setMaxResults(size);
            List<SeminarAnAbwesenheitDto> pagedResults = pagedQuery.getResultList();

            // Count query - must create new predicates
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Seminar> countRoot = countQuery.from(Seminar.class);
            Join<Seminar, Projekt> countProjectJoin = countRoot.join("project", JoinType.INNER);

            countQuery.select(cb.count(countRoot));

            List<Predicate> countPredicates = buildPredicates(cb, countRoot, countProjectJoin, isAdmin, benutzerId,
                    isActive, projectName, seminarName, kursEndeSpaeterAls, kursEndeFrueherAls, verzoegerung, countQuery);

            if (!countPredicates.isEmpty()) {
                countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));
            }

            TypedQuery<Long> countTypedQuery = entityManager.createQuery(countQuery);
            Long totalSize = countTypedQuery.getSingleResult();

            return new PageImpl<>(pagedResults, PageRequest.of(page, size), totalSize);
        }
    }

    // Helper method to build predicates for both main and count queries
    private <T> List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Seminar> root, Join<Seminar, Projekt> projectJoin,
                                                boolean isAdmin, Integer benutzerId, Boolean isActive,
                                                String projectName, String seminarName,
                                                LocalDate kursEndeSpaeterAls, LocalDate kursEndeFrueherAls,
                                                Boolean verzoegerung, CriteriaQuery<T> query) {

        List<Predicate> predicates = new ArrayList<>();

        // filter by isActive
        if (isActive != null) {
            predicates.add(isActive ?
                    cb.equal(root.get("status"), Status.ACTIVE) :
                    cb.equal(root.get("status"), Status.INACTIVE));
        }

        // filter by projectName
        if (projectName != null && !projectName.isEmpty()) {
            predicates.add(cb.like(cb.lower(projectJoin.get("bezeichnung")), "%" + projectName.toLowerCase() + "%"));
        }

        // filter by seminarName
        if (seminarName != null && !seminarName.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get("bezeichnung")), "%" + seminarName.toLowerCase() + "%"));
        }

        // filter by kursEndeSpaeterAls (end date after this date)
        if (kursEndeSpaeterAls != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("endDate"), kursEndeSpaeterAls));
        }

        // filter by kursEndeFrueherAls (end date before this date)
        if (kursEndeFrueherAls != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("endDate"), kursEndeFrueherAls));
        }

        //  verzoegerung (Optional)
        if (verzoegerung != null) {
            predicates.add(verzoegerung ?
                    cb.lessThan(root.get("endDate"), LocalDate.now()) :
                    cb.greaterThanOrEqualTo(root.get("endDate"), LocalDate.now()));
        }

        //if not admin, return only the assigned seminare
        if (!isAdmin) {
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Seminar2Trainer> seminarTrainerRoot = subquery.from(Seminar2Trainer.class);
            subquery.select(seminarTrainerRoot.get("seminar").get("id"))
                    .where(cb.equal(seminarTrainerRoot.get("trainer").get("id"), benutzerId));

            predicates.add(cb.in(root.get("id")).value(subquery));
        }

        return predicates;
    }
}
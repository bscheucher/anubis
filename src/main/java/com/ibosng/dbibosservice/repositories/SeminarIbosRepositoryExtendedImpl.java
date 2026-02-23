package com.ibosng.dbibosservice.repositories;

import com.ibosng.dbibosservice.entities.SeminarIbos;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SeminarIbosRepositoryExtendedImpl implements SeminarIbosRepositoryExtended {
    private final EntityManagerFactory entityManagerFactory;

    public SeminarIbosRepositoryExtendedImpl(@Qualifier("mariaDbEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<SeminarIbos> findSeminarByBezeichnung1InAndDatumVonAndDatumBisAndZeitVon(Set<String> identifiers, LocalDate datumVonParam, LocalDate datumBisParam, LocalTime zeitVonParam) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<SeminarIbos> query = cb.createQuery(SeminarIbos.class);
            Root<SeminarIbos> seminarIbos = query.from(SeminarIbos.class);

            Path<String> sMbezeichnung1 = seminarIbos.get("bezeichnung1");
            Path<String> datumVon = seminarIbos.get("datumVon");
            Path<String> datumBis = seminarIbos.get("datumBis");
            Path<String> zeitVon = seminarIbos.get("zeitVon");

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
            query.select(seminarIbos)
                    .where(cb.and(predicates.toArray(new Predicate[predicates.size()])));

            return entityManager.createQuery(query)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }
}

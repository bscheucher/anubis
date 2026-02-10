package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.lhr.Abmeldung;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.teilnehmer.Teilnehmer;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TeilnehmerSpecification {

    public static Specification<Teilnehmer> filterTeilnehmer(String searchTerm) {
        return (Root<Teilnehmer> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // Join personalnummer relation for case-insensitive search in personalnummer
            Join<Teilnehmer, Personalnummer> personalnummerJoin = root.join("personalnummer", JoinType.LEFT);
            // Check if the searchTerm is provided
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm.toLowerCase() + "%";

                // Create OR predicates for each field
                Predicate vornamePredicate = cb.like(cb.lower(root.get("vorname")), searchPattern);
                Predicate nachnamePredicate = cb.like(cb.lower(root.get("nachname")), searchPattern);
                Predicate svNummerPredicate = cb.like(cb.lower(root.get("svNummer").as(String.class)), searchPattern);

                // Join personalnummer relation for case-insensitive search in personalnummer
                Predicate personalnummerPredicate = cb.like(cb.lower(personalnummerJoin.get("personalnummer")), searchPattern);

                // Combine all fields with OR
                Predicate combinedPredicate = cb.or(vornamePredicate, nachnamePredicate, svNummerPredicate, personalnummerPredicate);
                predicates.add(combinedPredicate);
            }

            // Additional filter for 'isUeba' being true
            predicates.add(cb.equal(root.get("isUeba"), true));

            // Additional filter for onboarded on ibosNG being true
            predicates.add(cb.equal(personalnummerJoin.get("isIbosngOnboarded"), true));

            // Additional filter for being onboarded
            predicates.add(cb.isNotNull(personalnummerJoin.get("onboardedOn")));

            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Abmeldung> abmeldungRoot = subquery.from(Abmeldung.class);

            // Add condition to find Abmeldung rows matching the current Personalnummer
            subquery.select(abmeldungRoot.get("id"));
            subquery.where(cb.equal(abmeldungRoot.get("personalnummer").get("id"), personalnummerJoin.get("id")));

            // Exclude Teilnehmer with entries in Abmeldung
            predicates.add(cb.not(cb.exists(subquery)));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
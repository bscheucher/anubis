package com.ibosng.dbservice.repositories.vereinbarung;

import com.ibosng.dbservice.entities.masterdata.IbisFirma;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.Stammdaten;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.Vereinbarung;
import com.ibosng.dbservice.entities.mitarbeiter.vereinbarung.VereinbarungStatus;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VereinbarungSpecification {
    public static Specification<Vereinbarung> filterVereinbarung(String firmaName, String searchTerm, List<VereinbarungStatus> vereinbarungStatuses) {
        return (Root<Vereinbarung> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Join Firma to filter by its name
            Join<Vereinbarung, IbisFirma> firmaJoin = root.join("firma", JoinType.LEFT);
            if (firmaName != null && !firmaName.isEmpty()) {
                predicates.add(cb.like(cb.lower(firmaJoin.get("name")), "%" + firmaName.toLowerCase() + "%"));
            }

            // Apply Filters, else return all
            if(vereinbarungStatuses != null && !vereinbarungStatuses.isEmpty()){
                predicates.add(root.get("status").in(vereinbarungStatuses));
            }

            // Join Personalnummer (valid because Vereinbarung has a direct relationship)
            Join<Vereinbarung, Personalnummer> personalnummerJoin = root.join("personalnummer", JoinType.LEFT);

            // If searchTerm is provided, search in vorname, nachname, svnr, and personalnummer
            if (searchTerm != null && !searchTerm.isEmpty()) {
                String searchPattern = "%" + searchTerm.toLowerCase() + "%";

                Root<Stammdaten> stammdatenRoot = query.from(Stammdaten.class);
//
                Predicate vornamePredicate = cb.like(cb.lower(stammdatenRoot.get("vorname")), searchPattern.toLowerCase());
                Predicate nachnamePredicate = cb.like(cb.lower(stammdatenRoot.get("nachname")), searchPattern.toLowerCase());
                Predicate svnrPredicate = cb.like(cb.lower(stammdatenRoot.get("svnr").as(String.class)), searchPattern);
                Predicate personalnummerPredicate = cb.like(cb.lower(personalnummerJoin.get("personalnummer").as(String.class)), searchPattern);
                // Join the predicates with OR
                Predicate orPredicate = cb.or(personalnummerPredicate, vornamePredicate, nachnamePredicate, svnrPredicate);
                predicates.add(orPredicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}





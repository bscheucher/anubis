package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.vertragsaenderung.VertragsaenderungOverviewDto;
import com.ibosng.dbservice.entities.masterdata.Kostenstelle;
import com.ibosng.dbservice.entities.masterdata.Personalnummer;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static com.ibosng.dbservice.utils.Parsers.isNullOrBlank;

@Slf4j
public class VertragsaenderungRepositoryExtendedImpl implements VertragsaenderungRepositoryExtended {
    private final EntityManagerFactory entityManagerFactory;

    public VertragsaenderungRepositoryExtendedImpl(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Page<VertragsaenderungOverviewDto> findAllOrderedAndFilteredForOverview(String searchTerm, List<String> statuses, Pageable pageable) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            // Create the CriteriaBuilder and CriteriaQuery objects
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<VertragsaenderungOverviewDto> query = cb.createQuery(VertragsaenderungOverviewDto.class);

            // Define the root for Vertragsaenderung
            Root<Vertragsaenderung> vertragsaenderungRoot = query.from(Vertragsaenderung.class);
            Join<Vertragsaenderung, Personalnummer> personalnummerJoin = vertragsaenderungRoot.join("personalnummer", JoinType.INNER);

            // Define the root for Stammdaten and Vertragsdaten
            Root<Stammdaten> stammdatenRoot = query.from(Stammdaten.class);
            Root<Vertragsdaten> vertragsdatenRoot = query.from(Vertragsdaten.class);
            Join<Vertragsdaten, Kostenstelle> kostenstelleJoin = vertragsdatenRoot.join("kostenstelle", JoinType.LEFT);

            List<Predicate> predicates = buildPredicatesForMaFilter(cb, vertragsaenderungRoot, personalnummerJoin, stammdatenRoot, vertragsdatenRoot, searchTerm, statuses);

            // Apply the predicates
            query.where(cb.and(predicates.toArray(new Predicate[0])));

            // Add sorting logic
            if (pageable.getSort().isSorted()) {
                List<Order> orders = new ArrayList<>();
                for (Sort.Order order : pageable.getSort()) {
                    Path<?> sortPath;
                    switch (order.getProperty()) {
                        case "nachname":
                            sortPath = stammdatenRoot.get("nachname");
                            break;
                        case "vorname":
                            sortPath = stammdatenRoot.get("vorname");
                            break;
                        case "svnr":
                            sortPath = stammdatenRoot.get("svnr");
                            break;
                        case "kostenstelle":
                            sortPath = kostenstelleJoin.get("bezeichnung");
                            break;
                        case "gueltigAb":
                            sortPath = vertragsaenderungRoot.get("gueltigAb");
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid sort property: " + order.getProperty());
                    }
                    orders.add(order.isAscending() ? cb.asc(sortPath) : cb.desc(sortPath));
                }
                query.orderBy(orders);
            }

            // Select the desired fields from the entities
            query.multiselect(
                    vertragsaenderungRoot.get("id").alias("id"),
                    personalnummerJoin.get("personalnummer").alias("personalnummer"),
                    stammdatenRoot.get("nachname").alias("nachname"),
                    stammdatenRoot.get("vorname").alias("vorname"),
                    stammdatenRoot.get("svnr").alias("svnr"),
                    vertragsaenderungRoot.get("gueltigAb").alias("gueltigAb"),
                    kostenstelleJoin.get("bezeichnung").alias("kostenstelle").alias("kostenstelle"),
                    vertragsaenderungRoot.get("status").as(String.class).alias("status"),
                    vertragsaenderungRoot.get("interneAnmerkung")
            );

            // Paginated query execution
            TypedQuery<VertragsaenderungOverviewDto> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());

            List<VertragsaenderungOverviewDto> resultList = typedQuery.getResultList();

            // Count total results
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Vertragsaenderung> countRoot = countQuery.from(Vertragsaenderung.class);
            Join<Vertragsaenderung, Personalnummer> countPersonalnummerJoin = countRoot.join("personalnummer", JoinType.INNER);
            Root<Stammdaten> countStammdatenRoot = countQuery.from(Stammdaten.class);
            Root<Vertragsdaten> countVertragsdatenRoot = countQuery.from(Vertragsdaten.class);
            List<Predicate> countPredicates = buildPredicatesForMaFilter(cb, countRoot, countPersonalnummerJoin, countStammdatenRoot, countVertragsdatenRoot, searchTerm, statuses);

            countQuery.select(cb.count(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));
            Long totalCount = entityManager.createQuery(countQuery).getSingleResult();


            // Return paginated result
            return new PageImpl<>(resultList, pageable, totalCount);
        } finally {
            entityManager.close();
        }
    }

    private List<Predicate> buildPredicatesForMaFilter(CriteriaBuilder cb,
                                                       Root<Vertragsaenderung> vertragsaenderungRoot,
                                                       Join<Vertragsaenderung, Personalnummer> personalnummerJoin,
                                                       Root<Stammdaten> stammdatenRoot,
                                                       Root<Vertragsdaten> vertragsdatenRoot,
                                                       String searchTerm,
                                                       List<String> statuses) {
        List<Predicate> predicates = new ArrayList<>();
        // Only get MA onboarded
        predicates.add(cb.equal(personalnummerJoin.get("mitarbeiterType"), MitarbeiterType.MITARBEITER));
        predicates.add(cb.isNotNull(personalnummerJoin.get("onboardedOn")));

        // Ensure you're linking the correct personalnummer
        predicates.add(cb.equal(vertragsaenderungRoot.get("personalnummer"), stammdatenRoot.get("personalnummer")));
        predicates.add(cb.equal(vertragsaenderungRoot.get("predecessor").get("id"), vertragsdatenRoot.get("id")));

        // Add searchTerm predicates
        if (!isNullOrBlank(searchTerm)) {
            String searchPattern = "%" + searchTerm.toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(stammdatenRoot.get("vorname")), searchPattern),
                    cb.like(cb.lower(stammdatenRoot.get("nachname")), searchPattern),
                    cb.like(cb.lower(stammdatenRoot.get("svnr").as(String.class)), searchPattern)
            ));
        }

        // Add status predicate
        if (statuses != null && !statuses.isEmpty()) {
            List<VertragsaenderungStatus> statusesList = statuses.stream().map(VertragsaenderungStatus::valueOf).toList();
            predicates.add(vertragsaenderungRoot.get("status").in(statusesList));
        }
        return predicates;
    }
}

package com.ibosng.dbservice.repositories.mitarbeiter;

import com.ibosng.dbservice.dtos.mitarbeiter.MAFilteredResultDto;
import com.ibosng.dbservice.dtos.mitarbeiter.MASearchCriteriaDto;
import com.ibosng.dbservice.entities.Adresse;
import com.ibosng.dbservice.entities.Benutzer;
import com.ibosng.dbservice.entities.masterdata.*;
import com.ibosng.dbservice.entities.mitarbeiter.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
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
public class StammdatenRepositoryExtendedImpl implements StammdatenRepositoryExtended {
    private final EntityManagerFactory entityManagerFactory;

    public StammdatenRepositoryExtendedImpl(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Predicate> getPredicateForMAOnboarding(CriteriaBuilder cb,
                                                       CriteriaQuery<?> query,
                                                       Root<Stammdaten> st,
                                                       Join<Stammdaten, Personalnummer> pn,
                                                       Root<Vertragsdaten> vd,
                                                       Join<Vertragsdaten, Kostenstelle> kos,
                                                       String mitarbeiterType) {
        List<Predicate> predicates = new ArrayList<>();
        // Define the root for Stammdaten


        //Only get specific MA Types if param is present
        if (mitarbeiterType != null && !mitarbeiterType.isBlank()) {
            pn.on(cb.equal(pn.get("mitarbeiterType"), mitarbeiterType));
        }
        pn.on(cb.equal(pn.get("isIbosngOnboarded"), true));
        pn.on(cb.isNull(pn.get("onboardedOn")));

        // Subquery to check if this Vertragsdaten is referenced as a 'successor'
        Subquery<Integer> subquery = query.subquery(Integer.class);
        Root<Vertragsaenderung> vdae = subquery.from(Vertragsaenderung.class);
        subquery.select(cb.literal(1)); // just needs to return something
        subquery.where(cb.equal(vdae.get("successor").get("id"), vd.get("id")));

        // Main query should only include Vertragsdaten for which no matching Vertragsaenderung exists
        predicates.add(cb.not(cb.exists(subquery)));

        // Ensure you're linking the correct personalnummer
        predicates.add(cb.equal(st.get("personalnummer"), vd.get("personalnummer")));
        return predicates;
    }

    @Override
    public Page<Object[]> findForBenutzerOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType, String benutzerEmail) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        List<Object[]> resultList = new ArrayList<>();
        try {
            entityManager = entityManagerFactory.createEntityManager();
            // Create the CriteriaBuilder and CriteriaQuery objects
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);
            Root<Stammdaten> st = query.from(Stammdaten.class);
            Join<Stammdaten, Personalnummer> pn = st.join("personalnummer", JoinType.INNER);
            Root<Vertragsdaten> vd = query.from(Vertragsdaten.class);

            // Apply the join for kostenstelle with LEFT JOIN
            Join<Vertragsdaten, Kostenstelle> kos = vd.join("kostenstelle", JoinType.LEFT);

            Predicate currentPredicates = cb.and(getPredicateForMAOnboarding(cb, query, st, pn, vd, kos, mitarbeiterType).toArray(new Predicate[0]));
            Predicate createdByPredicate = cb.equal(st.get("createdBy"), benutzerEmail);
            Predicate fuehrungskraftPredicate = cb.equal(vd.get("fuehrungskraft").get("email"), benutzerEmail);
            Predicate emailFilterPredicate = cb.or(createdByPredicate, fuehrungskraftPredicate);

            // Final combined predicate
            Predicate combinedPredicates = cb.and(currentPredicates, emailFilterPredicate);

            if (benutzerEmail != null) {
                // Apply all predicates
                query.where(cb.and(combinedPredicates));
            } else {
                query.where(cb.and(currentPredicates));
            }


            // Select the desired fields from the entities
            query.multiselect(
                    pn.get("personalnummer"),
                    st.get("nachname"),
                    st.get("vorname"),
                    st.get("svnr"),
                    vd.get("eintritt"),
                    kos.get("bezeichnung")
            );

            // Apply sorting from pageable
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> {
                String property = order.getProperty();
                boolean ascending = order.isAscending();

                if (property.equalsIgnoreCase("nachname")) {
                    orders.add(cb.asc(st.get(property)));
                    orders.add(cb.asc(vd.get("eintritt")));
                } else if (!property.equalsIgnoreCase("kostenstelle") && !property.equalsIgnoreCase("eintritt")) {
                    orders.add(ascending ? cb.asc(st.get(property)) : cb.desc(st.get(property)));
                } else if (property.equalsIgnoreCase("kostenstelle")) {
                    Path<?> kostenstellePath = kos.get("bezeichnung");
                    orders.add(ascending ? cb.asc(kostenstellePath) : cb.desc(kostenstellePath));
                } else if (property.equalsIgnoreCase("eintritt")) {
                    orders.add(ascending ? cb.asc(vd.get("eintritt")) : cb.desc(vd.get("eintritt")));
                }
            });

            // Apply ordering if any sorting is provided
            if (!orders.isEmpty()) {
                query.orderBy(orders);
            }

            // Create the query and apply pagination
            Query finalQuery = entityManager.createQuery(query);
            finalQuery.setFirstResult((int) pageable.getOffset());
            finalQuery.setMaxResults(pageable.getPageSize());

            // Execute the query
            resultList = finalQuery.getResultList();

            // Fetch total count (without pagination)
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Stammdaten> countSt = countQuery.from(Stammdaten.class);
            Join<Stammdaten, Personalnummer> countPn = countSt.join("personalnummer", JoinType.INNER);
            Root<Vertragsdaten> countVd = countQuery.from(Vertragsdaten.class);
            Join<Vertragsdaten, Kostenstelle> countKos = vd.join("kostenstelle", JoinType.LEFT);

            // Apply all predicates
            countQuery.where(cb.and(getPredicateForMAOnboarding(cb, countQuery, countSt, countPn, countVd, countKos, mitarbeiterType).toArray(new Predicate[0])));

            // Set count query selection
            countQuery.select(cb.count(countSt));

            // Execute count query
            long total = entityManager.createQuery(countQuery).getSingleResult();

            return new PageImpl<>(resultList, pageable, total);
        } catch (Exception ex) {
            log.error("Exception caught while trying to get sorted and ordered MA: {}", ex.getMessage());
        } finally {
            entityManager.close();
        }
        return new PageImpl<>(resultList, pageable, 0);
    }

    @Override
    public Page<Object[]> findAllOrderedByNachnameEintritt(Pageable pageable, String mitarbeiterType) {

        return findForBenutzerOrderedByNachnameEintritt(pageable, mitarbeiterType, null);
    }

    @Override
    public Page<MAFilteredResultDto> findMAByCriteria(MASearchCriteriaDto maSearchCriteriaDto, Pageable pageable) {

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<MAFilteredResultDto> query = cb.createQuery(MAFilteredResultDto.class);

            // Create the root for Stammdaten
            Root<Stammdaten> root = query.from(Stammdaten.class);
            Join<Stammdaten, Personalnummer> personalnummerJoin = root.join("personalnummer", JoinType.LEFT);
            //Join the vertragsdaten
            Root<Vertragsdaten> vertragsdatenRoot = query.from(Vertragsdaten.class);

            // Apply the join for kostenstelle with LEFT JOIN. We need the join for the sorting, that is why we are defining it outside the if condition
            Join<Vertragsdaten, Kostenstelle> kostenstelleJoin = vertragsdatenRoot.join("kostenstelle", JoinType.LEFT);
            Join<Vertragsdaten, Benutzer> fuehrungskraftJoin = vertragsdatenRoot.join("fuehrungskraft", JoinType.LEFT);

            List<Predicate> predicates = buildPredicatesForMAFiltered(cb, root, personalnummerJoin, vertragsdatenRoot, query, kostenstelleJoin, maSearchCriteriaDto);


            // Sorting
            Expression<String> fuehrungskraftExpression = cb.concat(
                    cb.concat(fuehrungskraftJoin.get("firstName"), " "),
                    fuehrungskraftJoin.get("lastName")
            );

            if (pageable.getSort().isSorted()) {
                List<Order> orders = new ArrayList<>();
                for (Sort.Order order : pageable.getSort()) {
                    Expression<String> sortExpression;
                    if ("kostenstelle".equals(order.getProperty())) {
                        sortExpression = kostenstelleJoin.get("bezeichnung");
                    } else if ("fuehrungskraft".equals(order.getProperty())) {
                        sortExpression = fuehrungskraftExpression;
                    } else if ("svnr".equals(order.getProperty())) {
                        sortExpression = root.get("svnr");
                    } else {
                        sortExpression = root.get("nachname");
                    }
                    orders.add(order.isAscending() ? cb.asc(sortExpression) : cb.desc(sortExpression));
                }
                query.orderBy(orders);
            }
            // Select specific fields
            query.multiselect(
                    personalnummerJoin.get("personalnummer").alias("personalnummer"),
                    cb.concat(cb.concat(root.get("vorname"), " "), root.get("nachname")).alias("name"),
                    kostenstelleJoin.get("bezeichnung").alias("kostenstelle"),
                    fuehrungskraftExpression.alias("fuehrungskraft"),
                    root.get("svnr").alias("svnr"),
                    root.get("nachname").alias("nachname")

            );
            query.where(cb.and(predicates.toArray(new Predicate[0])));
            query.distinct(true);

            // Execute paginated query
            TypedQuery<MAFilteredResultDto> typedQuery = entityManager.createQuery(query);
            typedQuery.setFirstResult((int) pageable.getOffset()); // Offset
            typedQuery.setMaxResults(pageable.getPageSize()); // Limit

            List<MAFilteredResultDto> resultList = typedQuery.getResultList();

            // Count total results for pagination
            CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
            Root<Stammdaten> countRoot = countQuery.from(Stammdaten.class);
            Join<Stammdaten, Personalnummer> countPersonalnummerJoin = countRoot.join("personalnummer", JoinType.LEFT);
            Root<Vertragsdaten> countVertragsdatenRoot = countQuery.from(Vertragsdaten.class);
            Join<Vertragsdaten, Kostenstelle> countKostenstelleJoin = countVertragsdatenRoot.join("kostenstelle", JoinType.LEFT);


            List<Predicate> countPredicates = buildPredicatesForMAFiltered(cb, countRoot, countPersonalnummerJoin, countVertragsdatenRoot, countQuery, countKostenstelleJoin, maSearchCriteriaDto);

            countQuery.select(cb.countDistinct(countRoot)).where(cb.and(countPredicates.toArray(new Predicate[0])));

            Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

            // Return paginated result
            return new PageImpl<>(resultList, pageable, totalCount);
        } finally {
            entityManager.close();
        }

    }

    private List<Predicate> buildPredicatesForMAFiltered(CriteriaBuilder cb,
                                                         Root<Stammdaten> root,
                                                         Join<Stammdaten, Personalnummer> personalnummerJoin,
                                                         Root<Vertragsdaten> vertragsdatenRoot,
                                                         CriteriaQuery<?> query,
                                                         Join<Vertragsdaten, Kostenstelle> kostenstelleJoin,
                                                         MASearchCriteriaDto maSearchCriteriaDto) {

        List<Predicate> predicates = new ArrayList<>();


        // Check if the searchTerm is provided
        if (maSearchCriteriaDto != null) {
            if (!isNullOrBlank(maSearchCriteriaDto.getSearchTerm())) {
                String searchPattern = "%" + maSearchCriteriaDto.getSearchTerm().toLowerCase() + "%";

                // Create OR predicates for each field
                Predicate vornamePredicate = cb.like(cb.lower(root.get("vorname")), searchPattern);
                Predicate nachnamePredicate = cb.like(cb.lower(root.get("nachname")), searchPattern);
                Predicate svNummerPredicate = cb.like(cb.lower(root.get("svnr").as(String.class)), searchPattern);

                // Join personalnummer relation for case-insensitive search in personalnummer
                Predicate personalnummerPredicate = cb.like(cb.lower(personalnummerJoin.get("personalnummer")), searchPattern);

                // Combine all fields with OR
                Predicate combinedPredicate = cb.or(vornamePredicate, nachnamePredicate, svNummerPredicate, personalnummerPredicate);
                predicates.add(combinedPredicate);
            }

            //Add predicate for already onboarded MA
            predicates.add(cb.isNotNull(personalnummerJoin.get("onboardedOn")));
            predicates.add(cb.equal(cb.lower(personalnummerJoin.get("mitarbeiterType")), MitarbeiterType.MITARBEITER.getValue()));

            // Add predicate to link Vertragsdaten to Personalnummer
            Predicate vertragsdatenPredicate = cb.equal(personalnummerJoin.get("id"), vertragsdatenRoot.get("personalnummer").get("id"));
            predicates.add(vertragsdatenPredicate);

            Subquery<Integer> subquery = query.subquery(Integer.class);
            Root<Vertragsaenderung> subRoot = subquery.from(Vertragsaenderung.class);
            subquery.select(cb.literal(1));
            subquery.where(cb.equal(subRoot.get("successor"), vertragsdatenRoot));

            // Predicate: Vertragsdaten should NOT be in the list of successors
            Predicate notReferencedAsSuccessor = cb.not(cb.exists(subquery));
            predicates.add(notReferencedAsSuccessor);


            //Add predicate for Wohnort
            if (!isNullOrBlank(maSearchCriteriaDto.getWohnort())) {
                String searchPattern = "%" + maSearchCriteriaDto.getWohnort().toLowerCase() + "%";
                Join<Stammdaten, Adresse> adresseJoin = root.join("adresse", JoinType.LEFT);
                Predicate wohnortPredicate = cb.like(cb.lower(adresseJoin.get("ort")), searchPattern);
                predicates.add(wohnortPredicate);
            }

            //Add predicate for Firma
            if (maSearchCriteriaDto.getFirmen() != null && !maSearchCriteriaDto.getFirmen().isEmpty()) {
                Join<Personalnummer, IbisFirma> firmaJoin = personalnummerJoin.join("firma", JoinType.LEFT);
                Predicate firmaPredicate = firmaJoin.get("name").in(maSearchCriteriaDto.getFirmen());
                predicates.add(firmaPredicate);
            }

            //Add predicate for the Kostenstellen
            if (maSearchCriteriaDto.getKostenstellen() != null && !maSearchCriteriaDto.getKostenstellen().isEmpty()) {
                Predicate kostenstellePredicate = kostenstelleJoin.get("bezeichnung").in(maSearchCriteriaDto.getKostenstellen());
                predicates.add(kostenstellePredicate);
            }

            //Add predicate for the Kategorien
            if (maSearchCriteriaDto.getKategorien() != null && !maSearchCriteriaDto.getKategorien().isEmpty()) {
                Join<Vertragsdaten, Kategorie> kategorieJoin = vertragsdatenRoot.join("kategorie", JoinType.LEFT);
                kategorieJoin.on(kategorieJoin.get("name").in(maSearchCriteriaDto.getKategorien()));
                predicates.add(cb.isNotNull(kategorieJoin.get("name")));
            }

            //Add predicate for the Jobbezeichnung
            if (maSearchCriteriaDto.getJobbezeichnungen() != null && !maSearchCriteriaDto.getJobbezeichnungen().isEmpty()) {
                Join<Vertragsdaten, Jobbeschreibung> jobbeschreibungJoin = vertragsdatenRoot.join("jobBezeichnung", JoinType.LEFT);
                jobbeschreibungJoin.on(jobbeschreibungJoin.get("name").in(maSearchCriteriaDto.getJobbezeichnungen()));
                predicates.add(jobbeschreibungJoin.get("name").in(maSearchCriteriaDto.getJobbezeichnungen()));
            }

            //Add predicate for Beschäftigungsstatus
            if (maSearchCriteriaDto.getBeschaeftigungstatusen() != null && !maSearchCriteriaDto.getBeschaeftigungstatusen().isEmpty()) {
                //Join the ArbeitszeitenInfo
                Root<ArbeitszeitenInfo> arbeitszeitenInfoRoot = query.from(ArbeitszeitenInfo.class);
                // Ensure correct linking
                Predicate arbeitszeitenLinkPredicate = cb.equal(vertragsdatenRoot.get("id"), arbeitszeitenInfoRoot.get("vertragsdaten").get("id"));
                predicates.add(arbeitszeitenLinkPredicate);

                // Apply the join for kostenstelle with LEFT JOIN
                Join<ArbeitszeitenInfo, Beschaeftigungsstatus> beschaeftigungsstatusJoin = arbeitszeitenInfoRoot.join("beschaeftigungsstatus", JoinType.LEFT);
                Predicate beschaeftigungsstatusPredicate = beschaeftigungsstatusJoin.get("name").in(maSearchCriteriaDto.getBeschaeftigungstatusen());
                predicates.add(beschaeftigungsstatusPredicate);
            }
        }

        return predicates;
    }
}

package com.ibosng.dbservice.repositories;

import com.ibosng.dbservice.entities.DataReferenceTemp;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DataReferenceTempSpecification {

    // Optional: Define a whitelist of allowed fields to avoid injection/mistakes
    private static final Set<String> ALLOWED_FIELDS = Set.of(
            "reference", "data1", "data2", "data3", "data4", "data5",
            "data6", "data7", "data8", "data9", "data10",
            "createdBy", "changedBy"
    );

    public static Specification<DataReferenceTemp> buildDynamicSpec(Map<String, String> filters) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            filters.forEach((field, value) -> {
                if (value != null && !value.isBlank() && ALLOWED_FIELDS.contains(field)) {
                    predicates.add(cb.equal(root.get(field), value));
                }
            });

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
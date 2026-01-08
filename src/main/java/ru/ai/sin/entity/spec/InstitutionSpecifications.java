package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.ai.sin.dto.institution.InstitutionFilterReq;
import ru.ai.sin.entity.InstitutionEnt;

import java.util.ArrayList;
import java.util.List;

public final class InstitutionSpecifications {

    private InstitutionSpecifications() {}

    public static Specification<InstitutionEnt> byFilters(
            InstitutionFilterReq institutionFilterReq
    ) {
        return (root, query, cb) -> {

            if (query == null || institutionFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (institutionFilterReq.studentId() != null) {
                predicates.add(
                        cb.equal(root.get("student").get("id"), institutionFilterReq.studentId())
                );
            }

            if (institutionFilterReq.educationId() != null) {
                predicates.add(
                        cb.equal(root.get("education").get("id"), institutionFilterReq.educationId())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

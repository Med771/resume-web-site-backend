package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import ru.ai.sin.dto.experience.ExperienceFilterReq;
import ru.ai.sin.entity.ExperienceEnt;

import java.util.ArrayList;
import java.util.List;

public final class ExperienceSpecifications {

    private ExperienceSpecifications() {}

    public static Specification<ExperienceEnt> byFilters(
            ExperienceFilterReq experienceFilterReq
    ) {
        return (root, query, cb) -> {

            if (query == null || experienceFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (experienceFilterReq.studentId() != null) {
                predicates.add(
                    cb.equal(root.get("student").get("id"), experienceFilterReq.studentId())
                );
            }

            if (experienceFilterReq.companyId() != null) {
                predicates.add(
                    cb.equal(root.get("company").get("id"), experienceFilterReq.companyId())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

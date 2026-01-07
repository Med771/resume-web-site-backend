package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import ru.ai.sin.dto.recruiter.RecruiterFilterReq;

import ru.ai.sin.entity.RecruiterEnt;

import java.util.ArrayList;
import java.util.List;

public final class RecruiterSpecifications {

    private RecruiterSpecifications() {}

    public static Specification<RecruiterEnt> byFilters(RecruiterFilterReq recruiterFilterReq) {
        return (root, query, cb) -> {

            if (query == null || recruiterFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (recruiterFilterReq.name() != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + recruiterFilterReq.name().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

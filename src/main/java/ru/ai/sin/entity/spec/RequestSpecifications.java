package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.entity.RequestEnt;

import java.util.ArrayList;
import java.util.List;

public final class RequestSpecifications {

    private RequestSpecifications() {}

    public static Specification<RequestEnt> byFilters(
            RequestFilterReq requestFilterReq
    ) {
        return (root, query, cb) -> {

            if (query == null || requestFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (requestFilterReq.results() != null && !requestFilterReq.results().isEmpty()) {
                predicates.add(
                    root.get("result").in(requestFilterReq.results())
                );
            }

            if (requestFilterReq.recruiterId() != null) {
                predicates.add(
                    cb.equal(root.get("recruiter").get("id"), requestFilterReq.recruiterId())
                );
            }

            if (requestFilterReq.studentId() != null) {
                predicates.add(
                    cb.equal(root.get("student").get("id"), requestFilterReq.studentId())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}


package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.ai.sin.dto.portfolio.PortfolioFilterReq;
import ru.ai.sin.entity.PortfolioEnt;

import java.util.ArrayList;
import java.util.List;

public final class PortfolioSpecifications {
    private PortfolioSpecifications() {}

    public static Specification<PortfolioEnt> byFilters(PortfolioFilterReq portfolioFilterReq) {
        return (root, query, cb) -> {

            if (query == null || portfolioFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (portfolioFilterReq.studentId() != null) {
                predicates.add(cb.equal(root.get("student").get("id"), portfolioFilterReq.studentId()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

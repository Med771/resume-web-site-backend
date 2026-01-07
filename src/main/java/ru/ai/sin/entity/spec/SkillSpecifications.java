package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.ai.sin.dto.skill.SkillFilterReq;
import ru.ai.sin.entity.SkillEnt;

import java.util.ArrayList;
import java.util.List;

public class SkillSpecifications {

    private SkillSpecifications() {}

    public static Specification<SkillEnt> byFilters(SkillFilterReq skillFilterReq) {
        return (root, query, cb) -> {

            if (query == null || skillFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (skillFilterReq.name() != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("name")),
                                "%" + skillFilterReq.name().toLowerCase() + "%"
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

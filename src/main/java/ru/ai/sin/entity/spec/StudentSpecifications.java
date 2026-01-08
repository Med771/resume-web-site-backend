package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import ru.ai.sin.dto.student.StudentFilterReq;

import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.StudentEnt;

import java.util.ArrayList;
import java.util.List;

public final class StudentSpecifications {

    private StudentSpecifications() {}

    public static Specification<StudentEnt> byFilters(StudentFilterReq studentFilterReq) {

        return (root, query, cb) -> {

            if (query == null || studentFilterReq == null) {
                return null;
            }

            query.distinct(true);

            List<Predicate> predicates = new ArrayList<>();

            if (studentFilterReq.course() != null && !studentFilterReq.course().isEmpty()) {
                predicates.add(root.get("course").in(studentFilterReq.course()));
            }

            if (studentFilterReq.busyness() != null && !studentFilterReq.busyness().isEmpty()) {
                predicates.add(root.get("busyness").in(studentFilterReq.busyness()));
            }

            if (studentFilterReq.bornBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("birthDate"), studentFilterReq.bornBefore()));
            }

            if (studentFilterReq.bornAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("birthDate"), studentFilterReq.bornAfter()));
            }

            if (studentFilterReq.specialitiesIds() != null && !studentFilterReq.specialitiesIds().isEmpty()) {
                predicates.add(root.get("speciality").get("id").in(studentFilterReq.specialitiesIds()));
            }

            if (studentFilterReq.skillsIds() != null && !studentFilterReq.skillsIds().isEmpty()) {
                Join<StudentEnt, SkillEnt> skillsJoin = root.join("skills", JoinType.INNER);

                predicates.add(skillsJoin.get("id").in(studentFilterReq.skillsIds()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }


}

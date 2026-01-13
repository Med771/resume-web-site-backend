package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Expression;
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

            if (studentFilterReq.findString() != null && !studentFilterReq.findString().isBlank()) {
                String filter = studentFilterReq.findString().toLowerCase();

                double threshold = 0.3;
                int len = filter.length();

                if (len <= 3) {threshold = 0.1;}
                else if (len <= 5) {threshold = 0.18;}
                else if (len <= 8) {threshold = 0.25;}

                Expression<String> fullName = cb.lower(cb.concat(
                        cb.concat(
                                root.get("userInformation").get("firstName"), " "),
                                root.get("userInformation").get("lastName"))
                );

                Expression<String> bio = cb.lower(root.get("bio"));

                Expression<Double> fullNameSimilarity = cb.function("word_similarity", Double.class, fullName, cb.literal(filter));

                Expression<Double> bioSimilarity = cb.function("similarity", Double.class, bio, cb.literal(filter));

                Predicate fullNameFuzzy = cb.greaterThanOrEqualTo(fullNameSimilarity, threshold);

                Predicate bioFuzzy = cb.greaterThanOrEqualTo(bioSimilarity, threshold);

                Predicate fullNameLike = cb.like(fullName, "%" + filter + "%");

                predicates.add(cb.or(fullNameFuzzy, fullNameLike, bioFuzzy));
            }

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

package ru.ai.sin.entity.spec;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.model.BusynessEnum;
import ru.ai.sin.entity.model.CourseEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class StudentSpecifications {

    public static Specification<StudentEnt> courseIn(Set<CourseEnum> courses) {
        return (root, query, cb) -> {
            if (courses == null || courses.isEmpty()) return null;
            return root.get("course").in(courses);
        };
    }

    public static Specification<StudentEnt> busynessIn(Set<BusynessEnum> busynesses) {
        return (root, query, cb) -> {
            if (busynesses == null || busynesses.isEmpty()) return null;
            return root.get("busyness").in(busynesses);
        };
    }

    public static Specification<StudentEnt> bornBefore(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("birthDate"), date);
    }

    public static Specification<StudentEnt> bornAfter(LocalDate date) {
        return (root, query, cb) ->
                date == null ? null : cb.greaterThan(root.get("birthDate"), date);
    }

    public static Specification<StudentEnt> hasSkills(List<Long> skillsIds) {
        return (root, query, cb) -> {
            if (skillsIds == null || skillsIds.isEmpty()) return null;
            Join<Object, Object> skills = root.join("skills", JoinType.INNER);
            return skills.get("id").in(skillsIds);
        };
    }

    public static Specification<StudentEnt> hasSpecialities(List<Long> specialitiesIds) {
        return (root, query, cb) -> {
            if (specialitiesIds == null || specialitiesIds.isEmpty()) return null;
            return root.get("speciality").get("id").in(specialitiesIds);
        };
    }
}

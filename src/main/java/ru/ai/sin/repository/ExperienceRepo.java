package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.ExperienceEnt;

import java.util.List;
import java.util.Set;

@Repository
public interface ExperienceRepo extends JpaRepository<ExperienceEnt, Long> {

    @EntityGraph(attributePaths = {"company", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    ExperienceEnt findWithCompanyAndStudentById(Long id);

    List<ExperienceEnt> findAllByCompanyId(Long companyId);

    Set<ExperienceEnt> findAllByCompanyIdIn(Set<Long> companyIds);

    @EntityGraph(attributePaths = {"student", "company"})
    Page<ExperienceEnt> findAll(
            Specification<ExperienceEnt> spec,
            Pageable pageable
    );

}

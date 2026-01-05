package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.InstitutionEnt;

@Repository
public interface InstitutionRepo extends JpaRepository<InstitutionEnt, Long> {

    @EntityGraph(attributePaths = {"education", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    InstitutionEnt findWithEducationAndStudentById(Long id);

    @EntityGraph(attributePaths = {"education", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<InstitutionEnt> findAll(
            Specification<InstitutionEnt> spec,
            Pageable pageable
    );
}

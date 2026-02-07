package ru.ai.sin.logic.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyEnt, Long> {

    @EntityGraph(attributePaths = {"experiences"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<CompanyEnt> findWithExperiencesById(Long id);

    Page<CompanyEnt> findAll(Specification<CompanyEnt> spec, Pageable pageable);
}

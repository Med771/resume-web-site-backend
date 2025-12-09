package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.lang.NonNull;

import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.CompanyEnt;

import java.util.Optional;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyEnt, Long> {

    @EntityGraph(attributePaths = {"experiences"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<CompanyEnt> findWithExperiencesById(Long id);

    Page<CompanyEnt> findAllByNameIgnoreCase(String name, Pageable pageable);

    @NonNull
    Page<CompanyEnt> findAll(@NonNull Pageable pageable);
}

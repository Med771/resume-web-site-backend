package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.lang.NonNull;

import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.PortfolioEnt;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PortfolioRepo extends JpaRepository<PortfolioEnt, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<PortfolioEnt> findById(@NonNull Long id);

    @NonNull
    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<PortfolioEnt> findAll(@NonNull Pageable pageable);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<PortfolioEnt> findAllByStudentId(UUID id, Pageable pageable);
}

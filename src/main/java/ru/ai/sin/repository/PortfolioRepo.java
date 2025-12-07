package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.PortfolioEnt;

import java.util.UUID;

@Repository
public interface PortfolioRepo extends JpaRepository<PortfolioEnt, Long> {

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    PortfolioEnt findWithStudentById(Long id);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    PortfolioEnt findByIdAndIsActiveTrue(Long id);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    PortfolioEnt findByLinkAndIsActiveTrue(String name);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<PortfolioEnt> findAllByIsActiveTrue(Pageable pageable);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<PortfolioEnt> findAllByStudentIdAndIsActiveTrue(UUID id, Pageable pageable);
}

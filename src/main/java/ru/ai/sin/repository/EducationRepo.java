package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.EducationEnt;

public interface EducationRepo extends JpaRepository<EducationEnt, Long> {

    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    EducationEnt findByIdAndIsActiveTrue(Long id);

    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    EducationEnt findByInstitutionIgnoreCase(String institution);

    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<EducationEnt> findAllByIsActiveTrue(Pageable pageable);
}

package ru.ai.sin.repository;

import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.SpecialityEnt;

@Repository
public interface SpecialityRepo extends JpaRepository<SpecialityEnt, Long> {

    @Nullable
    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    SpecialityEnt findByIdAndIsActiveTrue(long id);

    @Nullable
    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    SpecialityEnt findByName(String name);

    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<SpecialityEnt> findAllByIsActiveTrue(Pageable pageable);
}

package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.SpecialityEnt;

@Repository
public interface SpecialityRepo extends JpaRepository<SpecialityEnt, Long> {

    @NonNull
    @EntityGraph(attributePaths = {"skills"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<SpecialityEnt> findAll(@NonNull Pageable pageable);
}

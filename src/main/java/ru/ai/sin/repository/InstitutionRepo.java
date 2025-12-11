package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.InstitutionEnt;

import java.util.UUID;

@Repository
public interface InstitutionRepo extends JpaRepository<InstitutionEnt, Long> {

    @EntityGraph(attributePaths = {"education", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    InstitutionEnt findWithEducationAndStudentById(Long id);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<InstitutionEnt> findAllByEducationId(Long educationId, Pageable pageable);

    @EntityGraph(attributePaths = {"education"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<InstitutionEnt> findAllByStudentId(UUID studentId, Pageable pageable);

    @NonNull
    @EntityGraph(attributePaths = {"education", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<InstitutionEnt> findAll(@NonNull Pageable pageable);
}

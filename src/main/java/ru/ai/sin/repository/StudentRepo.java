package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.StudentEnt;

import java.util.Set;
import java.util.UUID;

@Repository
public interface StudentRepo extends JpaRepository<StudentEnt, UUID>, JpaSpecificationExecutor<StudentEnt> {

    @EntityGraph(attributePaths = {"speciality"}, type = EntityGraph.EntityGraphType.LOAD)
    StudentEnt findByIdAndIsActiveTrue(UUID id);

    @EntityGraph(attributePaths = {"speciality"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<StudentEnt> findAllByIsActiveTrue(Pageable pageable);

    @Query("SELECT s.skills FROM StudentEnt s WHERE s.id = :studentId")
    Set<SkillEnt> findSkillsByStudentId(UUID studentId);
}

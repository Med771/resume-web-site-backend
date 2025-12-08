package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.ExperienceEnt;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ExperienceRepo extends JpaRepository<ExperienceEnt, Long> {

    @EntityGraph(attributePaths = {"company", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    ExperienceEnt findWithCompanyAndStudentById(Long id);

    List<ExperienceEnt> findAllByCompanyId(Long companyId);

    Set<ExperienceEnt> findAllByCompanyIdIn(Set<Long> ids);

    @EntityGraph(attributePaths = {"student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<ExperienceEnt> findAllByCompanyId(Long companyId, Pageable pageable);

    @EntityGraph(attributePaths = {"company"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<ExperienceEnt> findAllByStudentId(UUID studentId, Pageable pageable);

    @EntityGraph(attributePaths = {"company", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<ExperienceEnt> findAll(Pageable pageable);
}

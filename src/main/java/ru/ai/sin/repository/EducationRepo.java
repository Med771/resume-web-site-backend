package ru.ai.sin.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.EducationEnt;

import java.util.List;

public interface EducationRepo extends JpaRepository<EducationEnt, Long> {

    @EntityGraph(attributePaths = {"skills", "institutions"})
    EducationEnt findByIdAndIsActiveTrue(Long id);

    @EntityGraph(attributePaths = {"skills", "institutions"})
    EducationEnt findByInstitutionIgnoreCase(String institution);

    @EntityGraph(attributePaths = {"skills", "institutions"})
    List<EducationEnt> findAllByIsActiveTrue(PageRequest pageRequest);
}

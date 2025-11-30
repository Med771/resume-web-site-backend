package ru.ai.sin.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.SkillEnt;

import java.util.List;
import java.util.Optional;

public interface SkillRepo extends JpaRepository<SkillEnt, Long> {

    Optional<SkillEnt> findByIdAndIsActiveTrue(Long id);

    Optional<SkillEnt> findByNameIgnoreCase(String name);

    List<SkillEnt> findAllByIsActiveTrue(PageRequest pageRequest);

    List<SkillEnt> findAllByIdIn(List<Long> ids);
}

package ru.ai.sin.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.SkillEnt;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SkillRepo extends JpaRepository<SkillEnt, Long> {

    Optional<SkillEnt> findByIdAndIsActiveTrue(Long id);

    Optional<SkillEnt> findByNameIgnoreCase(String name);

    Set<SkillEnt> findAllByIsActiveTrue(PageRequest pageRequest);

    Set<SkillEnt> findAllByIdIn(List<Long> ids);
}

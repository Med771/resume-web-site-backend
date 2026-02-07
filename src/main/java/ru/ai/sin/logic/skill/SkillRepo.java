package ru.ai.sin.logic.skill;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;

@Repository
public interface SkillRepo extends JpaRepository<SkillEnt, Long> {

    Set<SkillEnt> findAllByIdIn(Collection<Long> ids);

    Page<SkillEnt> findAll(Specification<SkillEnt> spec, Pageable pageable);
}

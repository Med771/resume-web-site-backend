package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.SkillEnt;

import java.util.Collection;
import java.util.Set;

@Repository
public interface SkillRepo extends JpaRepository<SkillEnt, Long> {

    Set<SkillEnt> findAllByIdIn(Collection<Long> ids);
}

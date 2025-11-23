package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.SkillEnt;

public interface SkillRepo extends JpaRepository<SkillEnt, Long> {
}

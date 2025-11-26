package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.ExperienceEnt;

public interface ExperienceRepo extends JpaRepository<ExperienceEnt, Long> {
}

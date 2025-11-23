package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.EducationEnt;

public interface EducationRepo extends JpaRepository<EducationEnt, Long> {
}

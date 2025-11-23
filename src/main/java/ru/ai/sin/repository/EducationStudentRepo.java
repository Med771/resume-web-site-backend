package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.EducationStudentEnt;

public interface EducationStudentRepo extends JpaRepository<EducationStudentEnt, Long> {
}

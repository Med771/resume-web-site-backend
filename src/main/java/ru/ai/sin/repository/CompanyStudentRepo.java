package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.CompanyStudentEnt;

public interface CompanyStudentRepo extends JpaRepository<CompanyStudentEnt, Long> {
}

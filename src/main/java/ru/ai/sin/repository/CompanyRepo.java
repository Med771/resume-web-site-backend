package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.CompanyEnt;

public interface CompanyRepo extends JpaRepository<CompanyEnt, Long> {
}

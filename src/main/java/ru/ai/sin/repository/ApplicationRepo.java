package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.ApplicationEnt;

public interface ApplicationRepo extends JpaRepository<ApplicationEnt, Long> {
}

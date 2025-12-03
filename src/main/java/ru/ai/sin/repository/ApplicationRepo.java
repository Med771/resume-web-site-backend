package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.ApplicationEnt;

@Repository
public interface ApplicationRepo extends JpaRepository<ApplicationEnt, Long> {
}

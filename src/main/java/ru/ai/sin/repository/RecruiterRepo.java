package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.RecruiterEnt;

import java.util.UUID;

public interface RecruiterRepo extends JpaRepository<RecruiterEnt, UUID> {
}

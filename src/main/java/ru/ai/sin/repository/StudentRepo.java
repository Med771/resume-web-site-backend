package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.StudentEnt;

import java.util.UUID;

public interface StudentRepo extends JpaRepository<StudentEnt, UUID> {
}

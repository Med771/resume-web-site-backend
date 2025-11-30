package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;
import java.util.UUID;

public interface StudentRepo extends JpaRepository<StudentEnt, UUID> {

    List<StudentEnt> findAllByIdIn(List<UUID> studentIds);
}

package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentRepo extends JpaRepository<StudentEnt, UUID> {

    List<StudentEnt> findAllByIdIn(List<UUID> studentIds);
}

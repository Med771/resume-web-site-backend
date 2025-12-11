package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.EducationEnt;


@Repository
public interface EducationRepo extends JpaRepository<EducationEnt, Long> {
}

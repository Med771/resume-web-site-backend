package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.SpecialityEnt;

public interface SpecialityRepo extends JpaRepository<SpecialityEnt, Long> {
}

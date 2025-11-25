package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.InstitutionEnt;

public interface InstitutionRepo extends JpaRepository<InstitutionEnt, Long> {
}

package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.InstitutionEnt;

public interface InstitutionRepo extends JpaRepository<InstitutionEnt, Long> {

    Page<InstitutionEnt> findAllByEducationId(Long educationId, Pageable pageable);
}

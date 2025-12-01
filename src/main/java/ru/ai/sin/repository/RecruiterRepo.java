package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.RecruiterEnt;

import java.util.UUID;

public interface RecruiterRepo extends JpaRepository<RecruiterEnt, UUID> {

    RecruiterEnt findByIdAndIsActiveTrue(UUID id);

    Page<RecruiterEnt> findAllByCompanyNameIgnoreCaseAndIsActiveTrue(String companyName, Pageable pageable);

    Page<RecruiterEnt> findAllByIsActiveTrue(Pageable pageable);
}

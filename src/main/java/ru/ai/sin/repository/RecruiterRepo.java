package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.RecruiterEnt;

import java.util.UUID;

@Repository
public interface RecruiterRepo extends JpaRepository<RecruiterEnt, UUID> {

    Page<RecruiterEnt> findAllByCompanyNameIgnoreCase(String companyName, Pageable pageable);
}

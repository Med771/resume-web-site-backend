package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.RecruiterEnt;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecruiterRepo extends JpaRepository<RecruiterEnt, UUID> {
    // Optional<RecruiterEnt> findByContactInformationTelegramUserId(String telegramUserId);
    Optional<RecruiterEnt> findByContactInformationTelegramUsername(String telegramUsername);

    Optional<RecruiterEnt> findByUserInformationEmail(String email);

    Page<RecruiterEnt> findAll(
            Specification<RecruiterEnt> spec,
            Pageable pageable);
}

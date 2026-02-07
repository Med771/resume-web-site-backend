package ru.ai.sin.logic.recruiter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RecruiterRepo extends JpaRepository<RecruiterEnt, UUID>, JpaSpecificationExecutor<RecruiterEnt> {
    Optional<RecruiterEnt> findByContactInformationTelegramUserId(String telegramUserId);

    Optional<RecruiterEnt> findByUserInformationEmail(String email);

    @NonNull
    Page<RecruiterEnt> findAll(Specification<RecruiterEnt> spec, @NonNull Pageable pageable);
}

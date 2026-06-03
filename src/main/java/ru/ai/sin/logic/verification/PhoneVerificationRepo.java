package ru.ai.sin.logic.verification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneVerificationRepo extends JpaRepository<PhoneVerificationEnt, UUID> {

    Optional<PhoneVerificationEnt> findByTelegramChatIdAndStatus(Long telegramChatId, ru.ai.sin.models.enums.PhoneVerificationStatus status);
}

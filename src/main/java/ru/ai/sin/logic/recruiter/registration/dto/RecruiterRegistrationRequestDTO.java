package ru.ai.sin.logic.recruiter.registration.dto;

import ru.ai.sin.models.enums.RecruiterRegistrationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RecruiterRegistrationRequestDTO(
        UUID id,
        String username,
        String name,
        String companyName,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String telegramUsername,
        RecruiterRegistrationStatus status,
        String rejectReason,
        LocalDateTime processedAt,
        String processedByUsername,
        UUID approvedUserId,
        LocalDateTime createdAt
) {
}

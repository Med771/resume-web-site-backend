package ru.ai.sin.logic.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.ai.sin.models.enums.AccountStatus;
import ru.ai.sin.models.enums.RoleEnum;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "AccountApprovalUserDTO")
public record AccountApprovalUserDTO(
        UUID id,
        String username,
        String name,
        RoleEnum role,
        AccountStatus accountStatus,
        UUID studentId,
        UUID recruiterId,
        LocalDateTime createdAt
) {
}

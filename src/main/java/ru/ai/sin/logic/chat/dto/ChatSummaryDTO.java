package ru.ai.sin.logic.chat.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatSummaryDTO(
        UUID id,
        UUID recruiterId,
        UUID studentId,
        String lastMessagePreview,
        LocalDateTime lastActivityAt,
        long unreadCount
) {
}

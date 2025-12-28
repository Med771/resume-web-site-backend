package ru.ai.sin.dto.telegram;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetTelegramUserIdReq(
        @NotBlank
        @Size(min = 1, max = 16, message = "Telegram user id must be less than 16 characters")
        String telegramUserId
) {
}


package ru.ai.sin.dto.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SetChatIdReq(
        @NotBlank
        @Pattern(regexp = "-?\\d{1,32}", message = "Chat ID must contain 1-32 digits and optional - at start")
        String chatId) {
}

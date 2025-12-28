package ru.ai.sin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RequestNewChatReq(
        @NotBlank
        @Size(min = 1, max = 16, message = "Chat id must be less than 16 characters")
        String chatId,

        @NotBlank
        @Size(min = 1, max = 255, message = "Chat url must be less than 255 characters")
        String chatUrl
) {
}

package ru.ai.sin.dto.application;

import jakarta.validation.constraints.NotBlank;

public record SetHistoryReq(
        @NotBlank
        String history) {
}

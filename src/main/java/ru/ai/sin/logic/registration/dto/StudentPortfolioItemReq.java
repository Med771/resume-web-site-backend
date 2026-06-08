package ru.ai.sin.logic.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record StudentPortfolioItemReq(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @Size(max = 255)
        String link,

        @Size(max = 2000)
        String additionalInfo
) {
}

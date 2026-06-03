package ru.ai.sin.logic.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "OnboardingStatusRes")
public record OnboardingStatusRes(
        boolean completed
) {
}

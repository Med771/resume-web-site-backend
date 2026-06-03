package ru.ai.sin.logic.recruiter.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "RecruiterOnboardingStatusRes")
public record OnboardingStatusRes(
        boolean completed
) {
}

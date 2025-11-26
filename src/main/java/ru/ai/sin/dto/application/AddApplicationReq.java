package ru.ai.sin.dto.application;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AddApplicationReq(
        @NotNull
        UUID recruiterId,

        @NotNull
        UUID studentId) {
}

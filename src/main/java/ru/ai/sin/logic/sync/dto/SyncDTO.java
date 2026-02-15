package ru.ai.sin.logic.sync.dto;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.models.enums.SyncTypeEnum;

import java.util.UUID;

public record SyncDTO(
        @NotNull
        SyncTypeEnum type,

        @NotNull
        UUID id
) {
}

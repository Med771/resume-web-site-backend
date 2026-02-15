package ru.ai.sin.logic.sync.dto;

import ru.ai.sin.models.enums.SyncTypeEnum;

public record SyncUpdateReq(
        String userId,
        SyncTypeEnum type
) {
}

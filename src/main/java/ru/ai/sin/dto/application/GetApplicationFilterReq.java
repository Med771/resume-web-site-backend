package ru.ai.sin.dto.application;

import ru.ai.sin.entity.model.ResultEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetApplicationFilterReq(
        ResultEnum result,

        UUID recruiterId,
        UUID studentId,

        LocalDateTime createdBefore,
        LocalDateTime createAfter) {
}

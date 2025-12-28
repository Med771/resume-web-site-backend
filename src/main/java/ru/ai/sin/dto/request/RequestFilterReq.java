package ru.ai.sin.dto.request;

import ru.ai.sin.entity.model.ResultEnum;

import java.util.List;
import java.util.UUID;

public record RequestFilterReq(
        List<ResultEnum> results,

        UUID recruiterId,
        UUID studentId
) {
}

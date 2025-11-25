package ru.ai.sin.dto.application;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.model.ResultEnum;

public record SetResultReq(
        @NotNull
        ResultEnum result,

        String resultMessage) {
}

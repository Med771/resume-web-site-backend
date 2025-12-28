package ru.ai.sin.dto.request;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.model.ResultEnum;

public record RequestUpdateStatusReq(
        @NotNull
        ResultEnum resultEnum
) {
}

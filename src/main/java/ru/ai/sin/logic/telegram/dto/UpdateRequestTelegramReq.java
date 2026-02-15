package ru.ai.sin.logic.telegram.dto;

import jakarta.validation.constraints.Size;
import ru.ai.sin.models.enums.ResultEnum;

public record UpdateRequestTelegramReq(
        ResultEnum result,

        Boolean hasRecruiterMessage,

        Boolean hasStudentMessage,

        @Size(max = 65535)
        String studentResponseText
) {
}

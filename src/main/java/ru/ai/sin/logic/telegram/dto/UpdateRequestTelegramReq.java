package ru.ai.sin.logic.telegram.dto;

import jakarta.validation.constraints.Size;
import ru.ai.sin.models.enums.ResultEnum;

public record UpdateRequestTelegramReq(
        @Size(max = 16)
        String chatId,

        ResultEnum result,

        Boolean hasRecruiterMessage,

        Boolean hasStudentMessage,

        @Size(max = 65535)
        String studentResponseText
) {
}

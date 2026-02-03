package ru.ai.sin.dto.request;

import jakarta.validation.constraints.Size;
import ru.ai.sin.entity.model.ResultEnum;

/**
 * Частичное обновление заявки по chatId (для бота).
 * Все поля опциональны — обновляются только переданные.
 * Флаги имеет смысл передавать только в true (идемпотентно).
 */
public record UpdateRequestByChatReq(

        @Size(max = 10000)
        String studentResponseText,

        Boolean hasRecruiterMessage,

        Boolean hasStudentMessage,

        ResultEnum result,

        @Size(max = 16)
        String chatId,

        @Size(max = 255)
        String chatUrl
) {
}

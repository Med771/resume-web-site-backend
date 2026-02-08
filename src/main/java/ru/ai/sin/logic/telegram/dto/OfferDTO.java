package ru.ai.sin.logic.telegram.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.lang.NonNull;
import ru.ai.sin.logic.recruiter.dto.RecruiterRes;
import ru.ai.sin.logic.student.dto.StudentRes;
import ru.ai.sin.entity.model.ResultEnum;

public record OfferDTO(
        long id,

        @Size(min = 1, max = 16, message = "Chat id must be less than 16 characters")
        String chatId,

        @NotNull
        ResultEnum result,

        @Size(min = 1, max = 255, message = "Chat url must be less than 255 characters")
        String chatUrl,

        @NonNull
        StudentRes studentRes,

        @NonNull
        RecruiterRes recruiterRes
) { }

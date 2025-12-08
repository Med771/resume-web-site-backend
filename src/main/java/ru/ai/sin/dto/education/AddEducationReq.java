package ru.ai.sin.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record AddEducationReq(
        @NotBlank
        @Size(min = 1, max = 255, message = "Institution must be less than 2000 characters")
        String institution,

        @NotBlank
        @Size(min = 1, max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        @NotBlank
        @Size(min = 1, max = 255, message = "Web Url must be less than 2000 characters")
        String webUrl,

        @NotNull
        List<Long> skillsIds) {
}

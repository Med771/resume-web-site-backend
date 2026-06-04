package ru.ai.sin.logic.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "PatchStudentMeReq")
public record PatchStudentMeReq(
        @Schema(description = "Согласие на показ на публичной витрине анонимам")
        Boolean publicProfileConsent,
        @Schema(description = "Отключить guided hints")
        Boolean hintsDisabled
) {
}

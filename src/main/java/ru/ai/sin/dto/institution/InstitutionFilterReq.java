package ru.ai.sin.dto.institution;

import java.util.UUID;

public record InstitutionFilterReq(
        UUID studentId,
        Long educationId) {
}

package ru.ai.sin.dto.experience;

import java.util.UUID;

public record ExperienceFilterReq(
        UUID studentId,
        Long companyId) {
}

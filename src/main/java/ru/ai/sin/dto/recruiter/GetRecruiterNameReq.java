package ru.ai.sin.dto.recruiter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record GetRecruiterNameReq(
        @NotBlank
        @Size(min = 1, max = 255, message = "Company name must be less than 255 characters")
        String companyName
) {
}

package ru.ai.sin.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record MergeCompanyReq(
        @NotBlank
        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String name) {
}

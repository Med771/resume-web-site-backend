package ru.ai.sin.dto.speciality;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SetSpecialityNameReq(
        @NotBlank
        @Size(min = 1, max = 255, message = "Set speciality name must be less than 255 characters")
        String name) {
}

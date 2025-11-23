package ru.ai.sin.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResultEnum {
    PENDING("pending"),
    ACCEPTED("accepted"),
    REJECTED("rejected"),
    COMPLETED("completed");

    private final String result;

    public static ResultEnum fromCourse(String result) {
        for (ResultEnum status : values()) {
            if (status.getResult().equals(result)) return status;
        }
        throw new IllegalArgumentException("Unknown code: " + result);
    }
}

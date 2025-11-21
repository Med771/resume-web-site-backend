package ru.ai.sin.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CourseEnum {
    FIRST("1"),
    SECOND("2"),
    THIRD("3"),
    FOURTH("4");

    private final String course;

    public static CourseEnum fromCourse(String course) {
        for (CourseEnum status : values()) {
            if (status.getCourse().equals(course)) return status;
        }
        throw new IllegalArgumentException("Unknown code: " + course);
    }
}

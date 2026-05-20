package ru.ai.sin.models.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AnalyticsEventType {
    PAGE_VIEW("PAGE_VIEW");

    private final String code;

    public static AnalyticsEventType fromCode(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("eventType required");
        }
        String c = raw.trim().toUpperCase();
        for (AnalyticsEventType v : values()) {
            if (v.code.equals(c)) {
                return v;
            }
        }
        throw new IllegalArgumentException("Unknown eventType: " + raw);
    }
}

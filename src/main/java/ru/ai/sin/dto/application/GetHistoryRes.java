package ru.ai.sin.dto.application;

import java.util.List;

public record GetHistoryRes(
        List<Message> messages) {

    record Message(
            String name,
            String message) { }
}

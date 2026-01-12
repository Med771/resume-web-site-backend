package ru.ai.sin.helper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChatCreateRequestDto(

        @JsonProperty("chat_name")
        String chatName) {}
package ru.ai.sin.helper.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record ChatCreateResponseDto(

        @JsonProperty("chat_id")
        long chatId,

        @JsonProperty("chat_title")
        String chatTitle,

        @JsonProperty("invite_link")
        String inviteLink,

        String message

) {}
package ru.ai.sin.entity.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import jakarta.validation.constraints.Pattern;

import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {

    @Column(name = "phone_number", length = 32)
    @Pattern(regexp = "\\+?\\d{1,15}", message = "Phone number must contain 1-15 digits and optional + at start")
    private String phoneNumber;

    @Column(name = "telegram_username", length = 32)
    private String telegramUsername;

    @Column(name = "telegram_user_id", unique = true, length = 32)
    private String telegramUserId;
}
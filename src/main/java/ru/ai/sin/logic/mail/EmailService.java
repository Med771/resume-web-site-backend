package ru.ai.sin.logic.mail;

/**
 * Отправка транзакционных писем (подтверждение регистрации, сброс пароля и т.д.).
 */
public interface EmailService {

    /**
     * Асинхронная отправка. При {@code app.mail.enabled=false} письмо не уходит, пишется INFO в лог.
     */
    void sendAsync(EmailMessage message);
}

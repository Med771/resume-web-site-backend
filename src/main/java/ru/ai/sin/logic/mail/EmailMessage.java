package ru.ai.sin.logic.mail;

/**
 * Простое исходящее письмо (текст и/или HTML).
 */
public record EmailMessage(
        String to,
        String subject,
        String textBody,
        String htmlBody
) {
    public EmailMessage {
        if (to == null || to.isBlank()) {
            throw new IllegalArgumentException("Recipient address is required");
        }
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("Subject is required");
        }
        boolean hasText = textBody != null && !textBody.isBlank();
        boolean hasHtml = htmlBody != null && !htmlBody.isBlank();
        if (!hasText && !hasHtml) {
            throw new IllegalArgumentException("At least one of textBody or htmlBody is required");
        }
    }

    public static EmailMessage text(String to, String subject, String textBody) {
        return new EmailMessage(to, subject, textBody, null);
    }

    public static EmailMessage html(String to, String subject, String htmlBody) {
        return new EmailMessage(to, subject, null, htmlBody);
    }
}

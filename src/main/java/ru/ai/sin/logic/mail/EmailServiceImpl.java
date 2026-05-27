package ru.ai.sin.logic.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.ai.sin.config.property.MailProperties;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final MailProperties mailProperties;

    @Nullable
    private final JavaMailSender mailSender;

    public EmailServiceImpl(
            MailProperties mailProperties,
            @Autowired(required = false) JavaMailSender mailSender
    ) {
        this.mailProperties = mailProperties;
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendAsync(EmailMessage message) {
        if (!mailProperties.isEnabled()) {
            log.info("Mail disabled (app.mail.enabled=false), skipped: to={} subject={}",
                    maskEmail(message.to()), message.subject());
            return;
        }
        if (mailSender == null) {
            log.error("Mail enabled but JavaMailSender is not configured. Set spring.mail.host and related properties.");
            return;
        }
        try {
            sendNow(message);
            log.info("Mail sent: to={} subject={}", maskEmail(message.to()), message.subject());
        } catch (MailException | MessagingException ex) {
            log.error("Failed to send mail to {}: {}", maskEmail(message.to()), ex.getMessage(), ex);
        }
    }

    void sendNow(EmailMessage message) throws MessagingException {
        MimeMessage mime = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");
        helper.setFrom(mailProperties.formattedFrom());
        helper.setTo(message.to().trim());
        helper.setSubject(message.subject());
        if (message.textBody() != null && !message.textBody().isBlank()) {
            if (message.htmlBody() != null && !message.htmlBody().isBlank()) {
                helper.setText(message.textBody(), message.htmlBody());
            } else {
                helper.setText(message.textBody(), false);
            }
        } else {
            helper.setText(message.htmlBody(), true);
        }
        mailSender.send(mime);
    }

    private static String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at <= 1) {
            return "***";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}

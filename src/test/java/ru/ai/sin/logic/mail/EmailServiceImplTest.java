package ru.ai.sin.logic.mail;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import ru.ai.sin.config.property.MailProperties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    private MailProperties mailProperties;
    private EmailServiceImpl service;

    @BeforeEach
    void setUp() {
        mailProperties = new MailProperties();
        mailProperties.setFromAddress("noreply@test.local");
        mailProperties.setFromName("Test");
    }

    @Test
    void sendAsync_whenDisabled_doesNotCallMailSender() {
        mailProperties.setEnabled(false);
        service = new EmailServiceImpl(mailProperties, mailSender);

        service.sendAsync(EmailMessage.text("user@example.com", "Hi", "Body"));

        verify(mailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void sendNow_sendsMimeMessage() throws Exception {
        mailProperties.setEnabled(true);
        service = new EmailServiceImpl(mailProperties, mailSender);
        when(mailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        service.sendNow(EmailMessage.text("user@example.com", "Subject", "Hello"));

        ArgumentCaptor<MimeMessage> captor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(captor.capture());
        assertThat(captor.getValue().getSubject()).isEqualTo("Subject");
    }
}

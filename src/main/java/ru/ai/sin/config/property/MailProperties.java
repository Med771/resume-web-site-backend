package ru.ai.sin.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "app.mail")
public class MailProperties {

    /**
     * Отправка писем. При {@code false} {@link ru.ai.sin.logic.mail.EmailService} только логирует (удобно для локальной разработки).
     */
    private boolean enabled = false;

    /**
     * Адрес отправителя (заголовок From), например {@code noreply@singularity-resume.ru}.
     */
    private String fromAddress = "noreply@localhost";

    /**
     * Отображаемое имя отправителя, например {@code Singularity Resume}.
     */
    private String fromName = "Singularity Resume";

    /**
     * Базовый URL фронта для ссылок в письмах (без завершающего слэша).
     */
    private String frontendBaseUrl = "http://localhost:5371";

    public String formattedFrom() {
        if (fromName == null || fromName.isBlank()) {
            return fromAddress;
        }
        return fromName.trim() + " <" + fromAddress.trim() + ">";
    }
}

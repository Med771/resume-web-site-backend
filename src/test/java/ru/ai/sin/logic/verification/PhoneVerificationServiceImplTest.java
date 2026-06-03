package ru.ai.sin.logic.verification;

import org.junit.jupiter.api.Test;
import ru.ai.sin.config.property.TelegramProperties;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneVerificationServiceImplTest {

    @Test
    void normalizePhone_russianLeading8() {
        assertThat(PhoneVerificationServiceImpl.normalizePhone("+7 (999) 000-11-22")).isEqualTo("79990001122");
        assertThat(PhoneVerificationServiceImpl.normalizePhone("89990001122")).isEqualTo("79990001122");
    }
}

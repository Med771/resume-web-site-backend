package ru.ai.sin.logic.verification;

import org.junit.jupiter.api.Test;
import ru.ai.sin.config.property.TelegramProperties;
import ru.ai.sin.models.enums.PhoneVerificationStatus;

import static org.assertj.core.api.Assertions.assertThat;

class PhoneVerificationServiceImplTest {

    @Test
    void normalizePhone_russianLeading8() {
        assertThat(PhoneVerificationServiceImpl.normalizePhone("+7 (999) 000-11-22")).isEqualTo("79990001122");
        assertThat(PhoneVerificationServiceImpl.normalizePhone("89990001122")).isEqualTo("79990001122");
    }

    @Test
    void confirmWithDevCode_acceptsConfiguredCode() {
        TelegramProperties props = new TelegramProperties();
        props.setAllowDevConfirm(true);
        props.setDevConfirmCode("7890");

        PhoneVerificationRepo repo = org.mockito.Mockito.mock(PhoneVerificationRepo.class);
        PhoneVerificationEnt ent = new PhoneVerificationEnt();
        ent.setId(java.util.UUID.randomUUID());
        ent.setPhoneNumber("79991234567");
        ent.setStatus(PhoneVerificationStatus.PENDING);
        ent.setCreatedAt(java.time.LocalDateTime.now());
        ent.setExpiresAt(java.time.LocalDateTime.now().plusMinutes(15));

        org.mockito.Mockito.when(repo.findById(ent.getId())).thenReturn(java.util.Optional.of(ent));
        org.mockito.Mockito.when(repo.save(org.mockito.ArgumentMatchers.any())).thenAnswer(i -> i.getArgument(0));

        PhoneVerificationServiceImpl service = new PhoneVerificationServiceImpl(
                repo, props, org.mockito.Mockito.mock(TelegramBotClient.class));

        var res = service.confirmWithDevCode(ent.getId(), "7890");
        assertThat(res.status()).isEqualTo(PhoneVerificationStatus.CONFIRMED);
    }
}

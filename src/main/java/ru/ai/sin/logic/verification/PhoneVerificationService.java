package ru.ai.sin.logic.verification;

import ru.ai.sin.logic.verification.dto.PhoneVerificationStartReq;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStartRes;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStatusRes;
import ru.ai.sin.models.enums.PhoneVerificationStatus;

import java.util.Map;
import java.util.UUID;

public interface PhoneVerificationService {

    PhoneVerificationStartRes startVerification(PhoneVerificationStartReq req);

    PhoneVerificationStatusRes getStatus(UUID verificationId);

    void requireConfirmed(UUID verificationId, String phoneNumber);

    void handleWebhookUpdate(Map<String, Object> update);
}

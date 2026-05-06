package com.biznopay.v1.usecase.payment.webhook;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.enums.PaymentStatus;

import java.util.UUID;

public record HandleWebhookInput(
        UUID paymentId,
        PaymentStatus status,
        String failureReason,
        String rawPayload,
        String signature,
        PaymentMethodType providerType
) {
}

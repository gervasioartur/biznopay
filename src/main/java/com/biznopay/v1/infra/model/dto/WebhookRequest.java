package com.biznopay.v1.infra.model.dto;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.enums.PaymentStatus;

import java.util.UUID;

public record WebhookRequest(
        UUID paymentId,
        String failureReason,
        String rawPayload,
        PaymentStatus status,
        PaymentMethodType providerType
) {
}
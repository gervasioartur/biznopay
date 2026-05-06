package com.biznopay.v1.usecase.payment.find;

import com.biznopay.v1.domain.entity.payment.PaymentId;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public record PaymentResponse(
         UUID id,
         String idempotencyKey,
         Long amountInCents,
         String currency,
         String description,
         PaymentStatus status,
         PaymentMethodDetails paymentMethodDetails,
         Optional<String>providerPaymentId,
         Optional<String> failureReason,
         LocalDateTime createdAt
) {
}

package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;

public record CreatePaymentInput(
        String idempotencyKey,
        Long amountInCents,
        String description,
        String phoneNumber
) {
}

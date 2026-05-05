package com.biznopay.v1.usecase.payment.create;

public record CreatePaymentInput(
        String idempotencyKey,
        Long amountInCents,
        String description,
        String phoneNumber
) {
}

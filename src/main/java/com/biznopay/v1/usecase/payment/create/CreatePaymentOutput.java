package com.biznopay.v1.usecase.payment.create;

import java.util.UUID;

public record CreatePaymentOutput(String message, UUID paymentId) {
}

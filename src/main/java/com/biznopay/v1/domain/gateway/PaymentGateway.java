package com.biznopay.v1.domain.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;

import java.util.Optional;

public interface PaymentGateway {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    Payment save(Payment payment);
}

package com.biznopay.v1.domain.repository;

import com.biznopay.v1.domain.entity.payment.Payment;

import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findByIdempotencyKey(String idempotencyKey);

    Payment save(Payment payment);
}

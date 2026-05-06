package com.biznopay.v1.domain.service;

import com.biznopay.v1.domain.entity.payment.Payment;

public interface PaymentProcessor {
    void processWithRetry(Payment payment);
}
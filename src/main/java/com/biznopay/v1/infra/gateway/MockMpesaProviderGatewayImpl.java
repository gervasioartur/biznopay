package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MockMpesaProviderGatewayImpl implements PaymentProviderGateway {

    private final AtomicInteger attemptCount = new AtomicInteger(0);

    @Override
    public String submit(Payment payment) {
        int attempt = attemptCount.incrementAndGet();
        if (attempt < 3) throw new RuntimeException("mpesa gateway unavailable - attempt " + attempt);
        attemptCount.set(0);
        return UUID.randomUUID().toString();
    }
}
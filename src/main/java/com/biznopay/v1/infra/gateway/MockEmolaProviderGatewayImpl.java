package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;

import java.util.UUID;

public class MockEmolaProviderGatewayImpl implements PaymentProviderGateway {
    @Override
    public String submit(Payment payment) {
        return UUID.randomUUID().toString();
    }
}

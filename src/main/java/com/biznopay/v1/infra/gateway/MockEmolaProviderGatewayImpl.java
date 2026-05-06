package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class MockEmolaProviderGatewayImpl implements PaymentProviderGateway {

    @Override
    public String submit(Payment payment) {
        return "emola_session_" + UUID.randomUUID();
    }

    @Override
    public boolean validateWebhookSignature(String payload, String signature) {
        return true;
    }
}
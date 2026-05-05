package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import org.springframework.stereotype.Component;

@Component
public class MockMpesaProviderGatewayImpl implements PaymentProviderGateway {
    @Override
    public String submit(Payment payment) {
        return "";
    }
}

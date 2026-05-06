package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentProviderGatewayFactoryImpl implements PaymentProviderGatewayFactory {
    private final Map<PaymentMethodType, PaymentProviderGateway> providers;

    public PaymentProviderGatewayFactoryImpl(MockMpesaProviderGatewayImpl mpesa, MockEmolaProviderGatewayImpl emola, MockMkeshProviderGatewayImpl mkesh) {
        this.providers = Map.of(
                PaymentMethodType.MPESA, mpesa,
                PaymentMethodType.EMOLA, emola,
                PaymentMethodType.MKESH, mkesh
        );
    }

    public PaymentProviderGateway getProvider(PaymentMethodType type) {
        return providers.get(type);
    }
}

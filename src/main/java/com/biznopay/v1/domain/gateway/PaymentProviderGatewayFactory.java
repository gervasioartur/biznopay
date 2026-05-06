package com.biznopay.v1.domain.gateway;

import com.biznopay.v1.domain.enums.PaymentMethodType;

public interface PaymentProviderGatewayFactory {
    PaymentProviderGateway getProvider(PaymentMethodType type);
}
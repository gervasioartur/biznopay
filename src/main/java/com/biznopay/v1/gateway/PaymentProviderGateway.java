package com.biznopay.v1.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;

public interface PaymentProviderGateway {
    String submit(Payment payment);
}

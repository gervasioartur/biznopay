package com.biznopay.v1.infra.config;

import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.usecase.payment.create.CreatePayment;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class PaymentConfig {
    private final PaymentGateway paymentGateway;
    private final PaymentProviderGateway paymentProviderGateway;

    @Bean
    public CreatePayment createPayment() {
        return new CreatePayment(paymentGateway,paymentProviderGateway);
    }
}

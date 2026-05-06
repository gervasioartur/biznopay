package com.biznopay.v1.infra.config;

import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
import com.biznopay.v1.usecase.payment.webhook.HandleWebhook;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class WebhookConfig {
    private final PaymentGateway paymentGateway;
    private final PaymentProviderGatewayFactory providerFactory;

    @Bean
    public HandleWebhook handleWebhook() {
        return new HandleWebhook(paymentGateway, providerFactory);
    }
}

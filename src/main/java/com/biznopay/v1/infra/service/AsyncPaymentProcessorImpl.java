package com.biznopay.v1.infra.service;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
import com.biznopay.v1.domain.service.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AsyncPaymentProcessorImpl implements PaymentProcessor {
    private final PaymentGateway paymentGateway;
    private final PaymentProviderGatewayFactory providerFactory;

    @Async
    @Override
    public void processWithRetry(Payment payment) {
        String errorMessage = "";

        while (payment.canRetry()) {
            try {
                PaymentProviderGateway provider = providerFactory.getProvider(payment.getPaymentMethodDetails().getType());
                String conversationId = provider.submit(payment);
                payment = payment.markAsProcessing(conversationId);
                paymentGateway.save(payment);
                return;
            } catch (Exception e) {
                errorMessage = e.getMessage();
                payment = payment.incrementRetry();
                paymentGateway.save(payment);
                waitBackoff(payment.getRetryCount());
            }
        }

        payment = payment.markAsFailed(errorMessage);
        paymentGateway.save(payment);
    }

    private void waitBackoff(int retryCount) {
        try {
            long waitMs = (long) Math.pow(2, retryCount) * 1000L;
            Thread.sleep(waitMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
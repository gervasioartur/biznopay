package com.biznopay.v1.usecase.payment.webhook;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.ResourceNotFoundException;
import com.biznopay.v1.domain.exception.UnauthorizedException;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;

public class HandleWebhook {
    private final PaymentGateway paymentGateway;
    private final PaymentProviderGatewayFactory providerFactory;

    public HandleWebhook(PaymentGateway paymentGateway, PaymentProviderGatewayFactory providerFactory) {
        this.paymentGateway = paymentGateway;
        this.providerFactory = providerFactory;
    }

    public void execute(HandleWebhookInput input) {
        Payment payment = paymentGateway
                .findById(input.paymentId())
                .orElseThrow(() -> new ResourceNotFoundException(input.paymentId().toString()));

        PaymentProviderGateway provider = providerFactory.getProvider(payment.getPaymentMethodDetails().getType());
        if (!provider.validateWebhookSignature(input.rawPayload(), input.signature()))
            throw new UnauthorizedException("invalid webhook signature");

        Payment updated = switch (input.status()) {
            case PaymentStatus.COMPLETED -> payment
                    .markAsCompleted(payment.getPaymentMethodDetails().getType().toString() + input.paymentId());
            case PaymentStatus.FAILED -> payment.markAsFailed(input.failureReason());
            default -> payment;
        };

        paymentGateway.save(updated);
    }
}
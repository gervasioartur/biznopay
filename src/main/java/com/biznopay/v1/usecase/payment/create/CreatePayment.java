package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.EmolaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MkeshPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
import com.biznopay.v1.domain.service.PaymentProcessor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CreatePayment {
    private final PaymentGateway paymentGateway;
    private final PaymentProviderGatewayFactory providerFactory;
    private final PaymentProcessor paymentProcessor;

    public CreatePayment(PaymentGateway paymentGateway, PaymentProviderGatewayFactory providerFactory, PaymentProcessor paymentProcessor) {
        this.paymentGateway = paymentGateway;
        this.providerFactory = providerFactory;
        this.paymentProcessor = paymentProcessor;
    }

    public CreatePaymentOutput execute(CreatePaymentInput input) {
        Payment payment = this.paymentGateway
                .findByIdempotencyKey(input.idempotencyKey())
                .orElseGet(() -> createAndPersistPayment(input));

        if (payment.getStatus() == PaymentStatus.COMPLETED && payment.getProviderPaymentId().isPresent())
            return new CreatePaymentOutput(payment.getProviderPaymentId().get(), payment.getId().value());

        if (payment.getStatus() == PaymentStatus.FAILED)
            return new CreatePaymentOutput("Payment failed", payment.getId().value());

        if (payment.getStatus() == PaymentStatus.PROCESSING)
            return new CreatePaymentOutput("Payment is being processed", payment.getId().value());

        return submitWithTimeout(payment);
    }

    private CreatePaymentOutput submitWithTimeout(Payment payment) {
        try {
            PaymentProviderGateway provider = providerFactory.getProvider(payment.getPaymentMethodDetails().getType());
            Payment finalPayment = payment;
            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> provider.submit(finalPayment));
            String conversationId = future.get(3, TimeUnit.SECONDS);
            payment = payment.markAsProcessing(conversationId);
            paymentGateway.save(payment);
            return new CreatePaymentOutput("Payment is being processed", payment.getId().value());
        } catch (Exception e) {
            payment = payment.markAsProcessing(null);
            paymentGateway.save(payment);
            paymentProcessor.processWithRetry(payment);
            return new CreatePaymentOutput("Payment is being processed, we will notify you", payment.getId().value());
        }
    }

    private Payment createAndPersistPayment(CreatePaymentInput input) {
        PaymentMethodDetails details = getDomainPaymentMethodType(input.paymentMethod(), input.phoneNumber());
        Payment payment = Payment.create(
                input.idempotencyKey(),
                input.amountInCents(),
                input.description(),
                details
        );
        return paymentGateway.save(payment);
    }

    private PaymentMethodDetails getDomainPaymentMethodType(PaymentMethodType type, String phoneNumber) {
        return switch (type) {
            case MPESA -> MpesaPaymentDetails.create(phoneNumber);
            case MKESH -> MkeshPaymentDetails.create(phoneNumber);
            case EMOLA -> EmolaPaymentDetails.create(phoneNumber);
        };
    }
}
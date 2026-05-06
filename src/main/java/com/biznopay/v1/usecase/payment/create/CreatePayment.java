package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.EmolaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MkeshPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;

public class CreatePayment {

    private final PaymentGateway paymentGateway;
    private final PaymentProviderGatewayFactory paymentProviderGatewayFactory;

    public CreatePayment(PaymentGateway paymentGateway, PaymentProviderGatewayFactory paymentProviderGatewayFactory) {
        this.paymentGateway = paymentGateway;
        this.paymentProviderGatewayFactory = paymentProviderGatewayFactory;
    }

    public CreatePaymentOutput execute(CreatePaymentInput input) {
        Payment payment = this.paymentGateway
                .findByIdempotencyKey(input.idempotencyKey())
                .orElseGet(() -> createAndPersisPayment(input));

        if (payment.getStatus() == PaymentStatus.COMPLETED && payment.getProviderPaymentId().isPresent())
            return new CreatePaymentOutput(payment.getProviderPaymentId().get(), payment.getId().value());

        if (payment.getStatus() == PaymentStatus.FAILED)
            return new CreatePaymentOutput("Payment  failed", payment.getId().value());

        return processWithRetry(payment);
    }

    private Payment createAndPersisPayment(CreatePaymentInput input) {
        PaymentMethodDetails paymentMethodDetails = this.getDomainPaymentMethodType(input.paymentMethod(), input.phoneNumber());
        Payment payment = Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(), paymentMethodDetails);
        return paymentGateway.save(payment);
    }

    private CreatePaymentOutput processWithRetry(Payment payment) {
        String errorMessage = "";
        while (payment.canRetry()) {
            try {
                payment = payment.markAsProcessing();
                paymentGateway.save(payment);

                PaymentProviderGateway provider = this.paymentProviderGatewayFactory.getProvider(payment.getPaymentMethodDetails().getType());
                String providerPaymentId = provider.submit(payment);

                payment = payment.markAsCompleted(providerPaymentId);
                paymentGateway.save(payment);

                return new CreatePaymentOutput(providerPaymentId, payment.getId().value());

            } catch (Exception e) {
                errorMessage = e.getMessage();
                payment = payment.incrementRetry();
                paymentGateway.save(payment);
            }
        }

        payment = payment.markAsFailed(errorMessage);
        paymentGateway.save(payment);
        throw new ServiceUnavailableException(payment.getId().value());
    }

    private PaymentMethodDetails getDomainPaymentMethodType(PaymentMethodType type, String phoneNumber) {
        return switch (type) {
            case MPESA -> MpesaPaymentDetails.create(phoneNumber);
            case MKESH -> MkeshPaymentDetails.create(phoneNumber);
            case EMOLA -> EmolaPaymentDetails.create(phoneNumber);
        };
    }
}

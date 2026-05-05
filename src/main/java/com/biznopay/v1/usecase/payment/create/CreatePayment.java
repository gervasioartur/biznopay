package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.repository.PaymentRepository;
import com.biznopay.v1.gateway.PaymentProviderGateway;

public class CreatePayment {

    private final PaymentRepository paymentRepository;
    private final PaymentProviderGateway paymentProviderGateway;

    public CreatePayment(PaymentRepository paymentRepository, PaymentProviderGateway paymentProviderGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentProviderGateway = paymentProviderGateway;
    }

    public CreatePaymentOutput execute(CreatePaymentInput input) {
        Payment  payment = this.paymentRepository
                .findByIdempotencyKey(input.idempotencyKey())
                .orElse(createAndPersisPayment(input));

        if (payment.getStatus() == PaymentStatus.COMPLETED && payment.getProviderPaymentId().isPresent())
            return new CreatePaymentOutput(payment.getProviderPaymentId().get());

        if (payment.getStatus() == PaymentStatus.FAILED)
            return new CreatePaymentOutput("Payment  failed");

        return processWithRetry(payment);
    }

    private Payment createAndPersisPayment(CreatePaymentInput input){
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create(input.phoneNumber());
        Payment payment = Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(),paymentMethodDetails);
        return  paymentRepository.save(payment);
    }

    private CreatePaymentOutput processWithRetry(Payment payment) {
        String errorMessage = "";
        while (payment.canRetry()) {
            try {
                payment = payment.markAsProcessing();
                paymentRepository.save(payment);

                String providerPaymentId = paymentProviderGateway.submit(payment);

                payment = payment.markAsCompleted(providerPaymentId);
                paymentRepository.save(payment);

                return new CreatePaymentOutput(providerPaymentId);

            } catch (Exception e) {
                errorMessage =  e.getMessage();
                payment = payment.incrementRetry();
                paymentRepository.save(payment);
            }
        }

        payment = payment.markAsFailed(errorMessage);
        paymentRepository.save(payment);
        throw new ServiceUnavailableException();
    }
}

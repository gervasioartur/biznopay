package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.repository.PaymentRepository;

import java.util.Optional;

public class CreatePayment {

    private final PaymentRepository paymentRepository;

    public CreatePayment(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public CreatePaymentOutput execute(CreatePaymentInput input) {
        Optional<Payment> existingPayment = paymentRepository.findByIdempotencyKey(input.idempotencyKey());
        Payment payment =  null;
        if (existingPayment.isPresent())  payment =  existingPayment.get();

        if(payment.getStatus() == PaymentStatus.COMPLETED && payment.getProviderPaymentId().isPresent())
            return new CreatePaymentOutput(payment.getProviderPaymentId().get());

        if(!payment.canRetry()){
            throw new ServiceUnavailableException();
        }
        return null;
    }
}

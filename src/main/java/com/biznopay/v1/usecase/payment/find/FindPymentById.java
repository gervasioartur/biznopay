package com.biznopay.v1.usecase.payment.find;

import com.biznopay.v1.domain.exception.ResourceNotFoundException;
import com.biznopay.v1.domain.gateway.PaymentGateway;

import java.util.UUID;

public class FindPymentById {
    private final PaymentGateway paymentGateway;

    public FindPymentById(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public PaymentResponse execute(UUID paymentId) {
         return this.paymentGateway.findById(paymentId)
                 .map(PaymentResponse::fromPayment)
                 .orElseThrow(()-> new ResourceNotFoundException("Payment"));
    }
}

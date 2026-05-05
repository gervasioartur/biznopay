package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.exception.ServiceUnavailableException;

public class CreatePayment {

    public CreatePaymentOutput execute(CreatePaymentInput input) {
        throw new ServiceUnavailableException();
    }
}

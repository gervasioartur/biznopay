package com.biznopay.v1.mocks;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.usecase.payment.create.CreatePaymentInput;

public class Mocks {
    public static Payment paymentMock() {
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create("any_phone_number");
        return Payment.create("any_idempotency_key", 100L, "any_description", paymentMethodDetails);
    }

    public static CreatePaymentInput createPaymentInputMock () {
        return new CreatePaymentInput("any_idempotency_key", 100L, "any_description", "any_phone_number");
    }
}

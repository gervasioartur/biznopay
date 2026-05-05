package com.biznopay.v1.mocks;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.usecase.payment.create.CreatePaymentInput;

import java.time.LocalDateTime;

public class Mocks {
    public static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2026, 5, 5, 10, 0, 0);

    public static Payment pendingPaymentMock() {
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create("847272727");
        return Payment.create("any_idempotency_key", 100L, "any_description", paymentMethodDetails);
    }

    public static Payment pendingPaymentMock(CreatePaymentInput input) {
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create("847272727");
        return Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(), paymentMethodDetails);
    }

    public static Payment maximumRetriesPaymentMock() {
        Payment payment = pendingPaymentMock();
        for (int i = 0; i <= 2; i++) payment = payment.incrementRetry();
        return payment;
    }

    public static Payment completedPaymentMock() {
        Payment payment = pendingPaymentMock();
        payment = payment.markAsCompleted("any_provider_payment_id");
        return payment;
    }

    public static Payment failedPaymentMock() {
        Payment payment = pendingPaymentMock();
        payment = payment.markAsFailed("any_reason");
        return payment;
    }

    public static CreatePaymentInput createPaymentInputMock() {
        return new CreatePaymentInput("any_idempotency_key", 100L, "any_description", "847272727", PaymentMethodType.MPESA);
    }
}

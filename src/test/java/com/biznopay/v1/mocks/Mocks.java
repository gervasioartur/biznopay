package com.biznopay.v1.mocks;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.EmolaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MkeshPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.infra.model.dto.CreatePaymentRequest;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentJpaEntity;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentMethodDetailsJpaEntity;
import com.biznopay.v1.usecase.payment.create.CreatePaymentInput;

import java.time.LocalDateTime;
import java.util.UUID;

public class Mocks {
    public static final LocalDateTime FIXED_DATE_TIME = LocalDateTime.of(2026, 5, 5, 10, 0, 0);

    public static Payment pendingMpesaPaymentMock() {
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create("847272727");
        return Payment.create("any_idempotency_key", 100L, "any_description", paymentMethodDetails);
    }

    public static Payment pendingMpesaPaymentMock(CreatePaymentInput input) {
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create("847272727");
        return Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(), paymentMethodDetails);
    }

    public static Payment pendingMkeshPaymentMock() {
        PaymentMethodDetails paymentMethodDetails = MkeshPaymentDetails.create("837272727");
        return Payment.create("any_idempotency_key", 100L, "any_description", paymentMethodDetails);
    }

    public static Payment pendingMkeshPaymentMock(CreatePaymentInput input) {
        PaymentMethodDetails paymentMethodDetails = EmolaPaymentDetails.create("877272727");
        return Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(), paymentMethodDetails);
    }

    public static Payment pendingEmolaPaymentMock() {
        PaymentMethodDetails paymentMethodDetails = EmolaPaymentDetails.create("877272727");
        return Payment.create("any_idempotency_key", 100L, "any_description", paymentMethodDetails);
    }

    public static Payment pendingEmolaPaymentMock(CreatePaymentInput input) {
        PaymentMethodDetails paymentMethodDetails = EmolaPaymentDetails.create("877272727");
        return Payment.create(input.idempotencyKey(), input.amountInCents(), input.description(), paymentMethodDetails);
    }

    public static Payment processingMpesaPaymentMock() {
        Payment payment = pendingMpesaPaymentMock();
        payment = payment.markAsProcessing();
        return payment;
    }


    public static Payment maximumRetriesMpesaPaymentMock() {
        Payment payment = pendingMpesaPaymentMock();
        for (int i = 0; i <= 3; i++) payment = payment.incrementRetry();
        return payment;
    }

    public static Payment maximumRetriesMkeshPaymentMock() {
        Payment payment = pendingMkeshPaymentMock();
        for (int i = 0; i <= 3; i++) payment = payment.incrementRetry();
        return payment;
    }

    public static Payment maximumRetriesEmolaPaymentMock() {
        Payment payment = pendingEmolaPaymentMock();
        for (int i = 0; i <= 3; i++) payment = payment.incrementRetry();
        return payment;
    }

    public static Payment completedMpesaPaymentMock() {
        Payment payment = pendingMpesaPaymentMock();
        payment = payment.markAsCompleted("any_provider_payment_id");
        return payment;
    }

    public static Payment failedMpesaPaymentMock() {
        Payment payment = pendingMpesaPaymentMock();
        payment = payment.markAsFailed("any_reason");
        return payment;
    }

    public static Payment failedMkeshPaymentMock() {
        Payment payment = pendingMkeshPaymentMock();
        payment = payment.markAsFailed("any_reason");
        return payment;
    }

    public static CreatePaymentInput createMpesaPaymentInputMock() {
        return new CreatePaymentInput("any_idempotency_key", 100L, "any_description", "847272727", PaymentMethodType.MPESA);
    }

    public static CreatePaymentInput createMkeshPaymentInputMock() {
        return new CreatePaymentInput("any_idempotency_key", 100L, "any_description", "837272727", PaymentMethodType.MKESH);
    }

    public static CreatePaymentInput createEmolaPaymentInputMock() {
        return new CreatePaymentInput("any_idempotency_key", 100L, "any_description", "877272727", PaymentMethodType.EMOLA);
    }


    public static CreatePaymentRequest createMkeshPaymentRequestMock() {
        return new CreatePaymentRequest(UUID.randomUUID().toString(), 100L, "any_description", "837272727", PaymentMethodType.MKESH);
    }

    public static CreatePaymentRequest createMPesaPaymentRequestMock() {
        return new CreatePaymentRequest("any_idempotency_key", 100L, "any_description", "847272727", PaymentMethodType.MPESA);
    }

    public static CreatePaymentRequest createMPesaPaymentRequestWithInvalidAmountMock() {
        return new CreatePaymentRequest(UUID.randomUUID().toString(), 10L, "any_description", "847272727", PaymentMethodType.MPESA);
    }

    public static CreatePaymentRequest createMPesaPaymentRequestWithInvalidPhoneNumberMock() {
        return new CreatePaymentRequest(UUID.randomUUID().toString(), 100L, "any_description", "887272727", PaymentMethodType.MPESA);
    }

    public static PaymentJpaEntity paymentJpaEntityMock(Payment payment, PaymentMethodDetails paymentMethodDetails) {
        PaymentMethodDetailsJpaEntity paymentMethodDetailsJpaEntity = new PaymentMethodDetailsJpaEntity(paymentMethodDetails.getType(),
                paymentMethodDetails.getPhoneNumber());
        return new PaymentJpaEntity(payment.getId().value(), payment.getIdempotencyKey(), payment.getAmountInCents(),
                payment.getCurrency(), payment.getDescription(), payment.getStatus(), paymentMethodDetailsJpaEntity,
                payment.getProviderPaymentId().orElse(null), payment.getFailureReason().orElse(null), payment.getRetryCount(),
                payment.getCreatedAt(), payment.getUpdatedAt());
    }
}

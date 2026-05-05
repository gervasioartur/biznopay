package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrowMissingRequiredFieldExceptionWhenIdempotencyKeyIsBlankOrNull(String idempotencyKey) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(idempotencyKey,
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails()));
    }

    @Test
    public void ShouldThrowMissingRequiredFieldExceptionWhenAmountInCentsIsNull() {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                null, Mocks.paymentMock().getDescription(), Mocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 99L, 7_500_001L})
    public void ShouldThrowInvalidAmountExceptionWhenAmountInCentsIsLessThanZeroOrLesThanMinValueOrGreaterThanMaxValue(Long amountInCents) {
        Assertions.assertThrows(InvalidAmountException.class, () -> Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                amountInCents, Mocks.paymentMock().getDescription(), Mocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @NullSource
    public void ShouldThrowMissingRequiredFieldExceptionWhenPaymentMethodDetailsIsNull(PaymentMethodDetails paymentMethodDetails) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(), paymentMethodDetails));
    }

    @Test
    public void ShouldCreatePaymentWithCorrectValues() {
        Payment payment = Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails());

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(Mocks.paymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(Mocks.paymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(Mocks.paymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(Mocks.paymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(Mocks.paymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(Mocks.paymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(Mocks.paymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(Mocks.paymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(Mocks.paymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(Mocks.paymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(Mocks.paymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(Mocks.paymentMock().canRetry(), payment.canRetry());
    }

    @Test
    public void ShouldMarkPaymentAsProcessing() {
        Payment payment = Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails());
        payment = payment.markAsProcessing();
        Assertions.assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsCompleted() {
        Payment payment = Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails());
        String providerPaymentId = "any_provider_payment_id";
        payment = payment.markAsCompleted(providerPaymentId);
        Assertions.assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsFailed() {
        Payment payment = Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails());
        String reason = "any_reason";
        payment = payment.markAsFailed(reason);
        Assertions.assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    public void ShouldIncrementRetryCount() {
        Payment payment = Payment.create(Mocks.paymentMock().getIdempotencyKey(),
                Mocks.paymentMock().getAmountInCents(), Mocks.paymentMock().getDescription(),
                Mocks.paymentMock().getPaymentMethodDetails());
        payment = payment.incrementRetry();
        Assertions.assertEquals(1, payment.getRetryCount());
    }
}

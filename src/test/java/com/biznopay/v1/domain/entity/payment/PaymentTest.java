package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.InvalidFieldException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
public class PaymentTest {

    @Test
    public void ShouldThrowInvalidFieldExceptionWhenIdIsNull() {
        Payment payment = Mocks.pendingPaymentMock();
        Assertions.assertThrows(InvalidFieldException.class, () -> Payment.with(null, "", 0L,
                "", "", PaymentStatus.PENDING, payment.getPaymentMethodDetails(), Optional.empty(),
                Optional.empty(), 0, LocalDateTime.now(), LocalDateTime.now()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrowMissingRequiredFieldExceptionWhenIdempotencyKeyIsBlankOrNull(String idempotencyKey) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(idempotencyKey,
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails()));
    }

    @Test
    public void ShouldThrowMissingRequiredFieldExceptionWhenAmountInCentsIsNull() {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                null, Mocks.pendingPaymentMock().getDescription(), Mocks.pendingPaymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 99L, 7_500_001L})
    public void ShouldThrowInvalidAmountExceptionWhenAmountInCentsIsLessThanZeroOrLesThanMinValueOrGreaterThanMaxValue(Long amountInCents) {
        Assertions.assertThrows(InvalidAmountException.class, () -> Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                amountInCents, Mocks.pendingPaymentMock().getDescription(), Mocks.pendingPaymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @NullSource
    public void ShouldThrowMissingRequiredFieldExceptionWhenPaymentMethodDetailsIsNull(PaymentMethodDetails paymentMethodDetails) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(), paymentMethodDetails));
    }

    @Test
    public void ShouldCreatePaymentWithCorrectValues() {
        Payment payment = Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails());

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(Mocks.pendingPaymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(Mocks.pendingPaymentMock().canRetry(), payment.canRetry());
    }

    @Test
    public void ShouldMarkPaymentAsProcessing() {
        Payment payment = Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails());
        payment = payment.markAsProcessing();
        Assertions.assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsCompleted() {
        Payment payment = Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails());
        String providerPaymentId = "any_provider_payment_id";
        payment = payment.markAsCompleted(providerPaymentId);
        Assertions.assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsFailed() {
        Payment payment = Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails());
        String reason = "any_reason";
        payment = payment.markAsFailed(reason);
        Assertions.assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    public void ShouldIncrementRetryCount() {
        Payment payment = Payment.create(Mocks.pendingPaymentMock().getIdempotencyKey(),
                Mocks.pendingPaymentMock().getAmountInCents(), Mocks.pendingPaymentMock().getDescription(),
                Mocks.pendingPaymentMock().getPaymentMethodDetails());
        payment = payment.incrementRetry();
        Assertions.assertEquals(1, payment.getRetryCount());
    }
}

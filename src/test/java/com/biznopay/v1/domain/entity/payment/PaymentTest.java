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
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(InvalidFieldException.class, () -> Payment.with(null, "", 0L,
                "", "", PaymentStatus.PENDING, payment.getPaymentMethodDetails(), Optional.empty(),
                Optional.empty(), 0, LocalDateTime.now(), LocalDateTime.now()));
    }

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrowMissingRequiredFieldExceptionWhenIdempotencyKeyIsBlankOrNull(String idempotencyKey) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(idempotencyKey,
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails()));
    }

    @Test
    public void ShouldThrowMissingRequiredFieldExceptionWhenAmountInCentsIsNull() {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                null, Mocks.pendingMpesaPaymentMock().getDescription(), Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 99L, 7_500_001L})
    public void ShouldThrowInvalidAmountExceptionWhenAmountInCentsIsLessThanZeroOrLesThanMinValueOrGreaterThanMaxValue(Long amountInCents) {
        Assertions.assertThrows(InvalidAmountException.class, () -> Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                amountInCents, Mocks.pendingMpesaPaymentMock().getDescription(), Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @NullSource
    public void ShouldThrowMissingRequiredFieldExceptionWhenPaymentMethodDetailsIsNull(PaymentMethodDetails paymentMethodDetails) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(), paymentMethodDetails));
    }

    @Test
    public void ShouldCreatePaymentWithCorrectValues() {
        Payment payment = Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails());

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().canRetry(), payment.canRetry());
    }

    @Test
    public void ShouldBuildPaymentWithCorrectValues() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        LocalDateTime createdAtAndUpdatedAt = Mocks.FIXED_DATE_TIME;
        payment = Payment.with(payment.getId().value(), payment.getIdempotencyKey(), payment.getAmountInCents(),
                payment.getCurrency(), payment.getDescription(), payment.getStatus(), payment.getPaymentMethodDetails(), Optional.empty(),
                Optional.empty(), 0, createdAtAndUpdatedAt, createdAtAndUpdatedAt);

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(Mocks.pendingMpesaPaymentMock().canRetry(), payment.canRetry());
        Assertions.assertEquals(createdAtAndUpdatedAt, payment.getCreatedAt());
        Assertions.assertEquals(createdAtAndUpdatedAt, payment.getUpdatedAt());
    }

    @Test
    public void ShouldMarkPaymentAsProcessing() {
        Payment payment = Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails());
        payment = payment.markAsProcessing();
        Assertions.assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
        Assertions.assertTrue(payment.canRetry());
    }

    @Test
    public void ShouldMarkPaymentAsCompleted() {
        Payment payment = Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails());
        String providerPaymentId = "any_provider_payment_id";
        payment = payment.markAsCompleted(providerPaymentId);
        Assertions.assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsFailed() {
        Payment payment = Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails());
        String reason = "any_reason";
        payment = payment.markAsFailed(reason);
        Assertions.assertEquals(PaymentStatus.FAILED, payment.getStatus());
        Assertions.assertFalse(payment.canRetry());
    }

    @Test
    public void ShouldIncrementRetryCount() {
        Payment payment = Payment.create(Mocks.pendingMpesaPaymentMock().getIdempotencyKey(),
                Mocks.pendingMpesaPaymentMock().getAmountInCents(), Mocks.pendingMpesaPaymentMock().getDescription(),
                Mocks.pendingMpesaPaymentMock().getPaymentMethodDetails());
        payment = payment.incrementRetry();
        payment = payment.incrementRetry();
        payment = payment.incrementRetry();
        payment = payment.incrementRetry();
        Assertions.assertEquals(4, payment.getRetryCount());
        Assertions.assertFalse(payment.canRetry());

    }
}

package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import com.biznopay.v1.mocks.DomainEntityMocks;
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
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails()));
    }

    @Test
    public void ShouldThrowMissingRequiredFieldExceptionWhenAmountInCentsIsNull() {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                null, DomainEntityMocks.paymentMock().getDescription(), DomainEntityMocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 99L, 7_500_001L})
    public void ShouldThrowInvalidAmountExceptionWhenAmountInCentsIsLessThanZeroOrLesThanMinValueOrGreaterThanMaxValue(Long amountInCents) {
        Assertions.assertThrows(InvalidAmountException.class, () -> Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                amountInCents, DomainEntityMocks.paymentMock().getDescription(), DomainEntityMocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @NullSource
    public void ShouldThrowMissingRequiredFieldExceptionWhenPaymentMethodDetailsIsNull(PaymentMethodDetails paymentMethodDetails) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(), paymentMethodDetails));
    }

    @Test
    public void ShouldCreatePaymentWithCorrectValues() {
        Payment payment = Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails());

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(DomainEntityMocks.paymentMock().canRetry(), payment.canRetry());
    }

    @Test
    public void ShouldMarkPaymentAsProcessing() {
        Payment payment = Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails());
        payment = payment.markAsProcessing();
        Assertions.assertEquals(PaymentStatus.PROCESSING, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsCompleted(){
        Payment payment = Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails());
        String providerPaymentId = "any_provider_payment_id";
        payment = payment.markAsCompleted(providerPaymentId);
        Assertions.assertEquals(PaymentStatus.COMPLETED, payment.getStatus());
    }

    @Test
    public void ShouldMarkPaymentAsFailed(){
        Payment payment = Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails());
        String reason = "any_reason";
        payment = payment.markAsFailed(reason);
        Assertions.assertEquals(PaymentStatus.FAILED, payment.getStatus());
    }

    @Test
    public void ShouldIncrementRetryCount(){
        Payment payment = Payment.create(DomainEntityMocks.paymentMock().getIdempotencyKey(),
                DomainEntityMocks.paymentMock().getAmountInCents(), DomainEntityMocks.paymentMock().getDescription(),
                DomainEntityMocks.paymentMock().getPaymentMethodDetails());
        payment = payment.incrementRetry();
        Assertions.assertEquals(1, payment.getRetryCount());
    }
}

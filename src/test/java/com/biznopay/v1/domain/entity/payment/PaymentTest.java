package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import com.biznopay.v1.mocks.PaymentMocks;
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
                PaymentMocks.paymentMock().getAmountInCents(), PaymentMocks.paymentMock().getDescription(),
                PaymentMocks.paymentMock().getPaymentMethodDetails()));
    }

    @Test
    public void ShouldThrowMissingRequiredFieldExceptionWhenAmountInCentsIsNull() {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(PaymentMocks.paymentMock().getIdempotencyKey(),
                null, PaymentMocks.paymentMock().getDescription(), PaymentMocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L, 99L, 7_500_001L})
    public void ShouldThrowInvalidAmountExceptionWhenAmountInCentsIsLessThanZeroOrLesThanMinValueOrGreaterThanMaxValue(Long amountInCents) {
        Assertions.assertThrows(InvalidAmountException.class, () -> Payment.create(PaymentMocks.paymentMock().getIdempotencyKey(),
                amountInCents, PaymentMocks.paymentMock().getDescription(), PaymentMocks.paymentMock().getPaymentMethodDetails()));
    }

    @ParameterizedTest
    @NullSource
    public void ShouldThrowMissingRequiredFieldExceptionWhenPaymentMethodDetailsIsNull(PaymentMethodDetails paymentMethodDetails) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> Payment.create(PaymentMocks.paymentMock().getIdempotencyKey(),
                PaymentMocks.paymentMock().getAmountInCents(), PaymentMocks.paymentMock().getDescription(), paymentMethodDetails));
    }

    @Test
    public void ShouldCreatePaymentWithCorrectValues() {
        Payment payment = Payment.create(PaymentMocks.paymentMock().getIdempotencyKey(),
                PaymentMocks.paymentMock().getAmountInCents(), PaymentMocks.paymentMock().getDescription(),
                PaymentMocks.paymentMock().getPaymentMethodDetails());

        Assertions.assertNotNull(payment.getId());
        Assertions.assertEquals(PaymentMocks.paymentMock().getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(PaymentMocks.paymentMock().getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(PaymentMocks.paymentMock().getCurrency(), payment.getCurrency());
        Assertions.assertEquals(PaymentMocks.paymentMock().getDescription(), payment.getDescription());
        Assertions.assertEquals(PaymentMocks.paymentMock().getStatus(), payment.getStatus());
        Assertions.assertEquals(PaymentMocks.paymentMock().getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getType());
        Assertions.assertEquals(PaymentMocks.paymentMock().getPaymentMethodDetails().getMinAmountInCents(), payment.getPaymentMethodDetails().getMinAmountInCents());
        Assertions.assertEquals(PaymentMocks.paymentMock().getPaymentMethodDetails().getMaxAmountInCents(), payment.getPaymentMethodDetails().getMaxAmountInCents());
        Assertions.assertEquals(PaymentMocks.paymentMock().getProviderPaymentId(), payment.getProviderPaymentId());
        Assertions.assertEquals(PaymentMocks.paymentMock().getFailureReason(), payment.getFailureReason());
        Assertions.assertEquals(PaymentMocks.paymentMock().getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(PaymentMocks.paymentMock().canRetry(), payment.canRetry());
    }
}

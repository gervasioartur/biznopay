package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import com.biznopay.v1.mocks.PaymentMocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
}

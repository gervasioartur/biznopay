package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentTest {

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrowMissingRequiredFieldExceptionWhenIdempotencyKeyIsBlankOrNull(String idempotencyKey) {
        String phoneNumber = "any_phone_number";
        PaymentMethodDetails paymentMethodDetails = MpesaPaymentDetails.create(phoneNumber);
        Assertions.assertThrows(MissingRequiredFieldException.class, () ->
            Payment.create(idempotencyKey, 100L, "description", paymentMethodDetails));
    }
}

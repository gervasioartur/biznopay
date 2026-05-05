package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.exception.InvalidFieldException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MkeshPaymentDetailsTests {
    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrownMissingRequiredFieldExceptionWhenPhoneNumberIsBlankOrNull(String phoneNumber) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> MkeshPaymentDetails.create(phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"any_phone_number", "876965252"})
    public void ShouldThrownInvalidFieldExceptionWhenPhoneNumberIsInvalid(String phoneNumber) {
        Assertions.assertThrows(InvalidFieldException.class, () -> MkeshPaymentDetails.create(phoneNumber));
    }

    @Test
    public void ShouldCreateMkeshPaymentDetailsSuccessfully() {
        String phoneNumber = "839676525";
        MkeshPaymentDetails mkeshPaymentDetails = MkeshPaymentDetails.create(phoneNumber);
        Assertions.assertNotNull(mkeshPaymentDetails);
        Assertions.assertEquals(phoneNumber, mkeshPaymentDetails.getPhoneNumber());
        Assertions.assertEquals(PaymentMethodType.MKESH, mkeshPaymentDetails.getType());
        Assertions.assertEquals(100L, mkeshPaymentDetails.getMinAmountInCents());
        Assertions.assertEquals(7_500_000L, mkeshPaymentDetails.getMaxAmountInCents());
    }
}

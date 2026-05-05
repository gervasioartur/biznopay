package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.exception.InvalidFieldException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MpesaPaymentDetailsTests {

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrownMissingRequiredFieldExceptionWhenPhoneNumberIsBlankOrNull(String phoneNumber) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> MpesaPaymentDetails.create(phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"any_phone_number", "876965252"})
    public void ShouldThrownInvalidFieldExceptionWhenPhoneNumberIsInvalid(String phoneNumber) {
        Assertions.assertThrows(InvalidFieldException.class, () -> MpesaPaymentDetails.create(phoneNumber));
    }
}

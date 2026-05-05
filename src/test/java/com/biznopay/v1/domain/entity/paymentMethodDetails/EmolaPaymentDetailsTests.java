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
public class EmolaPaymentDetailsTests {

    @ParameterizedTest
    @NullAndEmptySource
    public void ShouldThrownMissingRequiredFieldExceptionWhenPhoneNumberIsBlankOrNull(String phoneNumber) {
        Assertions.assertThrows(MissingRequiredFieldException.class, () -> EmolaPaymentDetails.create(phoneNumber));
    }

    @ParameterizedTest
    @ValueSource(strings = {"any_phone_number", "836965252"})
    public void ShouldThrownInvalidFieldExceptionWhenPhoneNumberIsInvalid(String phoneNumber) {
        Assertions.assertThrows(InvalidFieldException.class, () -> EmolaPaymentDetails.create(phoneNumber));
    }

    @Test
    public void ShouldCreateEmolaPaymentDetailsSuccessfully() {
        String phoneNumber = "879676525";
        EmolaPaymentDetails emolaPaymentDetails = EmolaPaymentDetails.create(phoneNumber);
        Assertions.assertNotNull(emolaPaymentDetails);
        Assertions.assertEquals(phoneNumber, emolaPaymentDetails.getPhoneNumber());
        Assertions.assertEquals(PaymentMethodType.EMOLA, emolaPaymentDetails.getType());
        Assertions.assertEquals(100L, emolaPaymentDetails.getMinAmountInCents());
        Assertions.assertEquals(7_500_000L, emolaPaymentDetails.getMaxAmountInCents());
    }
}

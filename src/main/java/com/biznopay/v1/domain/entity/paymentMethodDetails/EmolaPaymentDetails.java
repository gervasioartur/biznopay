package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.exception.InvalidFieldException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;

public class EmolaPaymentDetails extends PaymentMethodDetails {
    private static final Long MIN_AMOUNT_IN_CENTS = 100L;
    private static final Long MAX_AMOUNT_IN_CENTS = 7_500_000L;
    private final String phoneNumber;

    private EmolaPaymentDetails(String phoneNumber) {
        super(PaymentMethodType.EMOLA);
        this.phoneNumber = this.validatePhoneNumber(phoneNumber);
    }

    public static EmolaPaymentDetails create(String phoneNumber) {
        return new EmolaPaymentDetails(phoneNumber);
    }

    //VALIDATIONS
    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new MissingRequiredFieldException("E-Mola Phone number", "EmolaPaymentDetails");
        if (!phoneNumber.matches("^(86|87)\\d{7}$"))
            throw new InvalidFieldException("E-Mola phone number", "EmolaPaymentDetails");
        return phoneNumber;
    }
    //END VALIDATIONS

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public Long getMinAmountInCents() {
        return MIN_AMOUNT_IN_CENTS;
    }

    @Override
    public Long getMaxAmountInCents() {
        return MAX_AMOUNT_IN_CENTS;
    }
}

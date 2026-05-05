package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.exception.InvalidFieldException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;

public class MkeshPaymentDetails extends PaymentMethodDetails {
    private static final Long MIN_AMOUNT_IN_CENTS = 100L;
    private static final Long MAX_AMOUNT_IN_CENTS = 7_500_000L;
    private final String phoneNumber;

    private MkeshPaymentDetails(String phoneNumber) {
        super(PaymentMethodType.MKESH);
        this.phoneNumber = this.validatePhoneNumber(phoneNumber);
    }

    public static MkeshPaymentDetails create(String phoneNumber) {
        return new MkeshPaymentDetails(phoneNumber);
    }

    //VALIDATIONS
    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank())
            throw new MissingRequiredFieldException("M-Kesh Phone number", "MkeshPaymentDetails");
        if (!phoneNumber.matches("^(82|83)\\d{7}$"))
            throw new InvalidFieldException("M-Kesh phone number", "MkeshPaymentDetails");
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

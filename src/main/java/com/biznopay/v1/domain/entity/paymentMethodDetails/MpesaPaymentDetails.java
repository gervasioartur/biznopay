package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;

public class MpesaPaymentDetails extends PaymentMethodDetails {
    private static final Long MIN_AMOUNT_IN_CENTS = 100L;
    private static final Long MAX_AMOUNT_IN_CENTS = 7_500_000L;
    private final String phoneNumber;

    private MpesaPaymentDetails(String phoneNumber) {
        super(PaymentMethodType.MPESA);
        this.phoneNumber = this.validatePhoneNumber(phoneNumber);
    }

    public static MpesaPaymentDetails create(String phoneNumber) {
        return new MpesaPaymentDetails(phoneNumber);
    }

    //VALIDATIONS
    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isEmpty())
            throw new MissingRequiredFieldException("Phone number", "MpesaPaymentDetails");
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

package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;

public class MpesaPaymentDetails extends PaymentMethodDetails {
    private static final Long MIN_AMOUNT_IN_CENTS = 100L;
    private static final Long MAX_AMOUNT_IN_CENTS = 7_500_000L;
    private final String phoneNumber;

    private MpesaPaymentDetails(String phoneNumber) {
        super(PaymentMethodType.MPESA);
        this.phoneNumber = phoneNumber;
    }

    public static MpesaPaymentDetails create(String phoneNumber) {
        return new MpesaPaymentDetails(phoneNumber);
    }

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

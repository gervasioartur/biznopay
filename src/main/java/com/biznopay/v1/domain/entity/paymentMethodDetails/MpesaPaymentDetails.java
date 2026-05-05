package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;

public class MpesaPaymentDetails extends PaymentMethodDetails {
    private final String phoneNumber;

    private  MpesaPaymentDetails(String phoneNumber) {
        super(PaymentMethodType.MPESA);
        this.phoneNumber = phoneNumber;
    }

    public static MpesaPaymentDetails create(String phoneNumber) {
        return new MpesaPaymentDetails(phoneNumber);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

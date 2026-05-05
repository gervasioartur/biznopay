package com.biznopay.v1.domain.entity.paymentMethodDetails;

import com.biznopay.v1.domain.enums.PaymentMethodType;

public abstract class PaymentMethodDetails {
    private final PaymentMethodType type;

    protected PaymentMethodDetails(PaymentMethodType type) {
        this.type = type;
    }

    public PaymentMethodType getType() {
        return this.type;
    }

    public abstract Long getMinAmountInCents();

    public abstract Long getMaxAmountInCents();
}

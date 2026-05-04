package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.exception.InvalidFieldException;

import java.util.UUID;

public record PaymentId(UUID value) {
    public static final String entityName = "Payment";

    public PaymentId {
        if (value == null) throw new InvalidFieldException("id", entityName);
    }

    public static PaymentId newId() {
        return new PaymentId(UUID.randomUUID());
    }
}
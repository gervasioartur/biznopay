package com.biznopay.v1.domain.exception;

import com.biznopay.v1.domain.entity.payment.Payment;

public class InvalidAmountException extends RuntimeException {
    private final String CODE = "INVALID_AMOUNT";

    public InvalidAmountException(Long min, Long max) {
        super("The amount is invalid! The amount must be between " + min + " and " + max + " in cents");
    }
}

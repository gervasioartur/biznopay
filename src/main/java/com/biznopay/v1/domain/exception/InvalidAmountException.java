package com.biznopay.v1.domain.exception;

import com.biznopay.v1.domain.entity.payment.Payment;

public class InvalidAmountException extends RuntimeException {
    private final String CODE = "INVALID_AMOUNT";

    public InvalidAmountException() {
        super("The amount is invalid! The amount must be between " + Payment.MIN_AMOUNT_IN_CENTS + " and " + Payment.MAX_AMOUNT_IN_CENTS + " in cents");
    }
}

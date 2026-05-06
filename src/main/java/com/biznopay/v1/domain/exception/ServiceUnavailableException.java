package com.biznopay.v1.domain.exception;

import java.util.UUID;

public class ServiceUnavailableException extends RuntimeException {
    private final String code = "PAYMENT_FAILED";

    public ServiceUnavailableException(UUID paymentId) {
        super("Payment with id " + paymentId + " failed to process");
    }

    public String getCode() {
        return code;
    }

}
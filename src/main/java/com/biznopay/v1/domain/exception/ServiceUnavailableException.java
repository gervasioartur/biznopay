package com.biznopay.v1.domain.exception;

public class ServiceUnavailableException extends RuntimeException {
    private final String code = "PAYMENT_FAILED";

    public ServiceUnavailableException() {
        super("Service is unavailable! Please try again later.");
    }

    public String getCode() {
        return code;
    }
}
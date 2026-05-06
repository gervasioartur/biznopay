package com.biznopay.v1.domain.exception;

public class UnauthorizedException extends RuntimeException {
    private final String code = "UNAUTHORIZED";

    public UnauthorizedException(String message) {
        super(message);
    }

    public String getCode() {
        return code;
    }
}

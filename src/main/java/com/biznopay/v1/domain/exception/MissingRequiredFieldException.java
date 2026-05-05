package com.biznopay.v1.domain.exception;

public class MissingRequiredFieldException extends RuntimeException {
    private final String CODE = "MISSING_REQUIRED_FIELD";

    public MissingRequiredFieldException(String field, String entity) {
        super("The field " + field + " is required for " + entity + "!");
    }
}

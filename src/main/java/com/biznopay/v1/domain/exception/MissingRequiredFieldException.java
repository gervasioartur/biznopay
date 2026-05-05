package com.biznopay.v1.domain.exception;

public class MissingRequiredFieldException extends RuntimeException {

    public MissingRequiredFieldException(String field, String entity) {
        super("The field " + field + " is required for " + entity + "!");
    }
}

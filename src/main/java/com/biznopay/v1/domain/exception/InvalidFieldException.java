package com.biznopay.v1.domain.exception;

public class InvalidFieldException extends RuntimeException {

    public InvalidFieldException(String field, String entity) {
        super("The field " + field + " is invalid!");
    }
}

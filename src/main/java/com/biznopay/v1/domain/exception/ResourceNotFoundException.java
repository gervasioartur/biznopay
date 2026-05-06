package com.biznopay.v1.domain.exception;

public class ResourceNotFoundException extends RuntimeException{
    private final String CODE = "RESOURCE_NOT_FOUND";

    public ResourceNotFoundException(String entity) {
        super(entity + " was not found!");
    }

    public String getCode() {
        return CODE;
    }
}

package com.biznopay.v1.infra.handler;

import com.biznopay.v1.domain.exception.*;
import com.biznopay.v1.domain.vo.ApiError;
import com.biznopay.v1.domain.vo.ApiResponse;
import com.biznopay.v1.infra.util.FuncUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.Instant;


@ControllerAdvice
public class ApiExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleDomain(DomainException exception) {
        log.error("");
        ApiError error = new ApiError(exception.getCode(), exception.getMessage());
        return ResponseEntity.badRequest().body(FuncUtils.buildResponseBody(false, null, error));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ResponseEntity<ApiResponse<Object>> handleDomain(InvalidAmountException exception) {
        log.warn("Invalid amount");
        ApiError error = new ApiError(exception.getCode(), exception.getMessage());
        return ResponseEntity.unprocessableContent().body(FuncUtils.buildResponseBody(false, null, error));
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.UNPROCESSABLE_CONTENT)
    public ResponseEntity<ApiResponse<Object>> handleDomain(InvalidFieldException exception) {
        ApiError error = new ApiError(exception.getCode(), exception.getMessage());
        return ResponseEntity.unprocessableContent().body(FuncUtils.buildResponseBody(false, null, error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation error");

        ApiError error = new ApiError("VALIDATION_ERROR", errorMessage);
        return ResponseEntity.badRequest().body(FuncUtils.buildResponseBody(false, null, error));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(ResourceNotFoundException exception) {
        ApiError error = new ApiError(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(FuncUtils.buildResponseBody(false, null, error), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ResponseEntity<ApiResponse<Object>> handleServiceUnavailable(ServiceUnavailableException exception) {
        log.error("Payment service unavailable");
        ApiError error = new ApiError(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(FuncUtils.buildResponseBody(false, null, error), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(Exception exception) {
        log.error(exception.getMessage(), Instant.now());
        exception.printStackTrace();
        ApiError error = new ApiError("InternalSeverError", "Unexpected Error! Please try again later.");
        return ResponseEntity.internalServerError().body(FuncUtils.buildResponseBody(false, null, error));
    }
}
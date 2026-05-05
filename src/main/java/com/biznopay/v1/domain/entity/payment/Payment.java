package com.biznopay.v1.domain.entity.payment;

import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentStatus;
import com.biznopay.v1.domain.exception.InvalidAmountException;
import com.biznopay.v1.domain.exception.MissingRequiredFieldException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class Payment {
    public static final String ENTITY_NAME = "Payment";

    private final PaymentId id;
    private final String idempotencyKey;
    private final Long amountInCents;
    private final String currency;
    private final String description;
    private final PaymentStatus status;
    private final PaymentMethodDetails paymentMethodDetails;
    private final Optional<String> providerPaymentId;
    private final Optional<String> failureReason;
    private final int retryCount;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    private Payment(PaymentId id, String idempotencyKey, Long amountInCents, String currency, String description,
                    PaymentStatus status, PaymentMethodDetails paymentMethodDetails, Optional<String> providerPaymentId,
                    Optional<String> failureReason, int retryCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.idempotencyKey = this.validateIdempotencyKey(idempotencyKey);
        this.amountInCents = this.validateAmountInCents(amountInCents, paymentMethodDetails);
        this.currency = currency;
        this.description = description;
        this.status = status;
        this.paymentMethodDetails = this.validatePaymentMethodDetails(paymentMethodDetails);
        this.providerPaymentId = providerPaymentId;
        this.failureReason = failureReason;
        this.retryCount = retryCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static Payment create(String idempotencyKey, Long amountInCents, String description, PaymentMethodDetails paymentMethodDetails) {
        LocalDateTime now = LocalDateTime.now();
        return new Payment(PaymentId.newId(), idempotencyKey, amountInCents, "MZN", description, PaymentStatus.PENDING,
                paymentMethodDetails, Optional.empty(), Optional.empty(), 0, now, now
        );
    }

    public static Payment with(UUID uuid, String idempotencyKey, Long amountInCents, String currency, String description,
                               PaymentStatus status, PaymentMethodDetails paymentMethodDetails, Optional<String> providerPaymentId,
                               Optional<String> failureReason, int retryCount, LocalDateTime createdAt, LocalDateTime updatedAt) {
        PaymentId id = new PaymentId(uuid);
        return new Payment(id, idempotencyKey, amountInCents, currency, description, status, paymentMethodDetails,
                providerPaymentId, failureReason, retryCount, createdAt, updatedAt);
    }

    //VALIDATIONS
    private String validateIdempotencyKey(String idempotencyKey) {
        if (idempotencyKey == null || idempotencyKey.isBlank())
            throw new MissingRequiredFieldException("idempotencyKey", ENTITY_NAME);
        return idempotencyKey;
    }

    private Long validateAmountInCents(Long amountInCents, PaymentMethodDetails paymentMethodDetails) {
        this.validatePaymentMethodDetails(paymentMethodDetails);

        if (amountInCents == null)
            throw new MissingRequiredFieldException("amountInCents", ENTITY_NAME);
        if (amountInCents < paymentMethodDetails.getMinAmountInCents() || amountInCents > paymentMethodDetails.getMaxAmountInCents())
            throw new InvalidAmountException(paymentMethodDetails.getMinAmountInCents(), paymentMethodDetails.getMaxAmountInCents());
        return amountInCents;
    }

    private PaymentMethodDetails validatePaymentMethodDetails(PaymentMethodDetails paymentMethodDetails) {
        if (paymentMethodDetails == null)
            throw new MissingRequiredFieldException("paymentMethodDetails", ENTITY_NAME);
        return paymentMethodDetails;
    }
    //END VALIDATIONS

    public Payment markAsProcessing() {
        return new Payment(id, idempotencyKey, amountInCents, currency, description, PaymentStatus.PROCESSING,
                paymentMethodDetails, providerPaymentId, failureReason,
                retryCount, createdAt, LocalDateTime.now());
    }

    public Payment markAsCompleted(String providerPaymentId) {
        return new Payment(id, idempotencyKey, amountInCents, currency, description, PaymentStatus.COMPLETED,
                paymentMethodDetails, Optional.of(providerPaymentId), Optional.empty(),
                retryCount, createdAt, LocalDateTime.now());
    }

    public Payment markAsFailed(String reason) {
        return new Payment(id, idempotencyKey, amountInCents, currency, description,
                PaymentStatus.FAILED, paymentMethodDetails, providerPaymentId, Optional.of(reason),
                retryCount, createdAt, LocalDateTime.now());
    }

    public Payment incrementRetry() {
        return new Payment(id, idempotencyKey, amountInCents, currency, description,
                status, paymentMethodDetails, providerPaymentId, failureReason,
                retryCount + 1, createdAt, LocalDateTime.now());
    }

    public boolean canRetry() {
        return this.retryCount < 3 && this.status == PaymentStatus.PENDING || this.status == PaymentStatus.PROCESSING;
    }

    public PaymentId getId() {
        return id;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Long getAmountInCents() {
        return amountInCents;
    }

    public String getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public PaymentMethodDetails getPaymentMethodDetails() {
        return paymentMethodDetails;
    }

    public Optional<String> getProviderPaymentId() {
        return providerPaymentId;
    }

    public Optional<String> getFailureReason() {
        return failureReason;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
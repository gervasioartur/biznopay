package com.biznopay.v1.infra.model.dto;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record CreatePaymentRequest(
        @NotNull(message = "Field 'idempotencyKey' is required!")
        @NotBlank(message = "Field 'name' is required!")
        @NotEmpty(message = "Field 'name' is required!")
        String idempotencyKey,

        @NotNull(message = "Field 'amountInCents' is required!")
        Long amountInCents,
        String description,

        @NotNull(message = "Field 'phoneNumber' is required!")
        @NotBlank(message = "Field 'phoneNumber' is required!")
        @NotEmpty(message = "Field 'phoneNumber' is required!")
        String phoneNumber,

        PaymentMethodType paymentMethod
) {
}

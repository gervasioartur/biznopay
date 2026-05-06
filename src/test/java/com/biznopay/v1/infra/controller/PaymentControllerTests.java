package com.biznopay.v1.infra.controller;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.infra.model.dto.CreatePaymentRequest;
import com.biznopay.v1.infra.util.FuncUtils;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PaymentControllerTests extends E2ETestBase {
    @Test
    public void ShouldReturnUnprocessableContentIfAmountIsInvalid() {
        CreatePaymentRequest request = Mocks.createMPesaPaymentRequestWithInvalidAmountMock();

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @Test
    public void ShouldReturnUnprocessableContentIfPhoneNumberIsInvalid() {
        CreatePaymentRequest request = Mocks.createMPesaPaymentRequestWithInvalidPhoneNumberMock();

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_CONTENT);
    }

    @Test
    public void ShouldReturnServerUnavailableOnCreatePayment() {
        CreatePaymentRequest request = Mocks.createMkeshPaymentRequestMock();
        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }

    @Test
    public void ShouldReturnCreatedAfterRetryOnCreatePayment() {
        CreatePaymentRequest request = Mocks.createMPesaPaymentRequestMock();
        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void ShouldReturnBadRequestIfAmountIsMissing() {
        CreatePaymentRequest request = new CreatePaymentRequest("any_idempotency_key", null,
                "any_description", "847272727", PaymentMethodType.MPESA);

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void ShouldReturnBadRequestIfIdempotencyIsMissing() {
        CreatePaymentRequest request = new CreatePaymentRequest("", 100L,
                "any_description", "847272727", PaymentMethodType.MPESA);

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void ShouldReturnExistingPaymentForSameIdempotencyKey() {
        FuncUtils funcUtils = new FuncUtils();
        CreatePaymentRequest request = Mocks.createMPesaPaymentRequestMock();

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isCreated();

        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isCreated();
    }
}

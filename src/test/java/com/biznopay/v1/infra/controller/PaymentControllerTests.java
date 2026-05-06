package com.biznopay.v1.infra.controller;

import com.biznopay.v1.infra.model.dto.CreatePaymentRequest;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class PaymentControllerTests extends E2ETestBase {

    @Test
    public void ShouldReturnServerUnavailableOnCreatePayment() {
        CreatePaymentRequest request = Mocks.createMkeshPaymentRequestMock();
        restTestClient.post()
                .uri("/api/v1/payments")
                .body(request)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
    }
}

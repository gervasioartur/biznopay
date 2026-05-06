package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.mocks.Mocks;
import com.biznopay.v1.usecase.payment.webhook.HandleWebhook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MockMkeshProviderGatewayImplTests {

    @Test
    public void ShouldThrowRuntimeExceptionOnSubmitFailure() {
        Payment payment = Mocks.processingMpesaPaymentMock();
        MockMkeshProviderGatewayImpl gateway = new MockMkeshProviderGatewayImpl();
        assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }
}


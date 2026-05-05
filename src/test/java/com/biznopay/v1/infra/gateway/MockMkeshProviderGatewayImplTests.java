package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MockMkeshProviderGatewayImplTests {

    @Test
    public void ShouldThrowRuntimeExceptionOnSubmitFailure() {
        Payment payment = Mocks.processingPaymentMock();
        MockMkeshProviderGatewayImpl gateway = new MockMkeshProviderGatewayImpl();
        assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }
}


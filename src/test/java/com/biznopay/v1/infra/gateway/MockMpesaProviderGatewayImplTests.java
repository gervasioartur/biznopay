package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockMpesaProviderGatewayImplTests {
    @Autowired
    private final MockMpesaProviderGatewayImpl gateway = new MockMpesaProviderGatewayImpl();

    @Test
    public void ShouldThrowExceptionOnSecondAttempt() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }

    @Test
    public void ShouldResetAfterSuccess() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
        gateway.submit(payment);
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }
}

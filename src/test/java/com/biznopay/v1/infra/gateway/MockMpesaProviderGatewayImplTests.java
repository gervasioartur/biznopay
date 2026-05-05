package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockMpesaProviderGatewayImplTests {
    private final MockMpesaProviderGatewayImpl gateway = new MockMpesaProviderGatewayImpl();

    @Test
    public void ShouldThrowExceptionOnFirstAttempt() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }

    @Test
    public void ShouldThrowExceptionOnSecondAttempt() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
    }

    @Test
    public void ShouldReturnProviderPaymentIdOnThirdAttempt() {
        Payment payment = Mocks.pendingMpesaPaymentMock();
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
        Assertions.assertThrows(RuntimeException.class, () -> gateway.submit(payment));
        String providerPaymentId = gateway.submit(payment);
        Assertions.assertNotNull(providerPaymentId);
        Assertions.assertFalse(providerPaymentId.isBlank());
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

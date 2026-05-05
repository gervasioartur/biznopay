package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MockEmolaProviderGatewayImplTests {

    @Test
    public void ShouldReturnProviderPaymentIdOnSubmitSuccess() {
        Payment payment = Mocks.processingPaymentMock();
        String providerPaymentId = new MockEmolaProviderGatewayImpl().submit(payment);
        Assertions.assertNotNull(providerPaymentId);
    }
}

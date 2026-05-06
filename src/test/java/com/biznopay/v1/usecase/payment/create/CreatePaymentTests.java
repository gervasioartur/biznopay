package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
import com.biznopay.v1.domain.service.PaymentProcessor;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CreatePaymentTests {
    @Mock
    private PaymentGateway paymentGateway;
    @Mock
    private PaymentProviderGatewayFactory providerFactory;
    @Mock
    private PaymentProcessor paymentProcessor;
    @Mock
    private PaymentProviderGateway paymentProviderGateway;


    private CreatePayment setUp() {
        return new CreatePayment(paymentGateway, providerFactory, paymentProcessor);
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsCompleteAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createMpesaPaymentInputMock();
        Payment payment = Mocks.completedMpesaPaymentMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output.message());
        Assertions.assertEquals(payment.getProviderPaymentId().get(), output.message());
    }
}

package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGateway;
import com.biznopay.v1.domain.gateway.PaymentProviderGatewayFactory;
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
    private PaymentProviderGateway paymentProviderGateway;
    @Mock
    private PaymentProviderGatewayFactory paymentProviderGatewayFactory;


    private CreatePayment setUp() {
        return new CreatePayment(paymentGateway, paymentProviderGatewayFactory);
    }

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetriesMpesa() {
        CreatePaymentInput input = Mocks.createMpesaPaymentInputMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(Mocks.maximumRetriesMpesaPaymentMock()));
        CreatePayment createPayment = this.setUp();
        Assertions.assertThrows(ServiceUnavailableException.class, () -> createPayment.execute(input));
    }

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetriesMkesh() {
        CreatePaymentInput input = Mocks.createMkeshPaymentInputMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(Mocks.maximumRetriesMkeshPaymentMock()));
        CreatePayment createPayment = this.setUp();
        Assertions.assertThrows(ServiceUnavailableException.class, () -> createPayment.execute(input));
    }

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetriesEmola() {
        CreatePaymentInput input = Mocks.createEmolaPaymentInputMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(Mocks.maximumRetriesEmolaPaymentMock()));
        CreatePayment createPayment = this.setUp();
        Assertions.assertThrows(ServiceUnavailableException.class, () -> createPayment.execute(input));
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsCompleteAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createMpesaPaymentInputMock();
        Payment payment = Mocks.completedMpesaPaymentMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output.response());
        Assertions.assertEquals(payment.getProviderPaymentId().get(), output.response());
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsFailedAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createMpesaPaymentInputMock();
        Payment payment = Mocks.failedMpesaPaymentMock();
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output);
        Assertions.assertEquals("Payment  failed", output.response());
    }

    @Test
    public void ShouldCompletePaymentAfterRetryOnTransientFailureMpesa() {
        CreatePaymentInput input = Mocks.createMpesaPaymentInputMock();
        Payment payment = Mocks.pendingMpesaPaymentMock(input);
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.empty());
        Mockito.when(paymentGateway.save(Mockito.any())).thenReturn(payment);
        Mockito.when(paymentProviderGatewayFactory.getProvider(payment.getPaymentMethodDetails().getType())).thenReturn(paymentProviderGateway);
        Mockito.when(paymentProviderGateway.submit(Mockito.any())).thenThrow(new RuntimeException("gateway unavailable")).thenReturn("mpesa_txn_123");
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertEquals("mpesa_txn_123", output.response());
    }

    @Test
    public void ShouldCompletePaymentAfterRetryOnTransientFailureMkesh() {
        CreatePaymentInput input = Mocks.createMkeshPaymentInputMock();
        Payment payment = Mocks.pendingMkeshPaymentMock(input);
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.empty());
        Mockito.when(paymentGateway.save(Mockito.any())).thenReturn(payment);
        Mockito.when(paymentProviderGatewayFactory.getProvider(payment.getPaymentMethodDetails().getType())).thenReturn(paymentProviderGateway);
        Mockito.when(paymentProviderGateway.submit(Mockito.any())).thenThrow(new RuntimeException("gateway unavailable")).thenReturn("mkesh_txn_123");
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertEquals("mkesh_txn_123", output.response());
    }

    @Test
    public void ShouldCompletePaymentAfterRetryOnTransientFailureEmola() {
        CreatePaymentInput input = Mocks.createEmolaPaymentInputMock();
        Payment payment = Mocks.pendingEmolaPaymentMock(input);
        Mockito.when(paymentGateway.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.empty());
        Mockito.when(paymentGateway.save(Mockito.any())).thenReturn(payment);
        Mockito.when(paymentProviderGatewayFactory.getProvider(payment.getPaymentMethodDetails().getType())).thenReturn(paymentProviderGateway);
        Mockito.when(paymentProviderGateway.submit(Mockito.any())).thenThrow(new RuntimeException("gateway unavailable")).thenReturn("emola_txn_123");
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertEquals("emola_txn_123", output.response());
    }
}

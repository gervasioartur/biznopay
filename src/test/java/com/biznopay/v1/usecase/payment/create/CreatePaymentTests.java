package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.repository.PaymentRepository;
import com.biznopay.v1.gateway.PaymentProviderGateway;
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
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentProviderGateway paymentProviderGateway;

    private CreatePayment setUp() {
        return new CreatePayment(paymentRepository, paymentProviderGateway);
    }

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetries() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(Mocks.maximumRetriesPaymentMock()));
        CreatePayment createPayment = this.setUp();
        Assertions.assertThrows(ServiceUnavailableException.class, () -> createPayment.execute(input));
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsCompleteAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Payment payment = Mocks.completedPaymentMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output.response());
        Assertions.assertEquals(payment.getProviderPaymentId().get(), output.response());
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsFailedAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Payment payment = Mocks.failedPaymentMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output);
        Assertions.assertEquals("Payment  failed", output.response());
    }

    @Test
    public void ShouldCompletePaymentAfterRetryOnTransientFailure() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Payment payment = Mocks.pendingPaymentMock(input);
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.empty());
        Mockito.when(paymentRepository.save(Mockito.any())).thenReturn(payment);
        Mockito.when(paymentProviderGateway.submit(Mockito.any())).thenThrow(new RuntimeException("gateway unavailable")).thenReturn("mpesa_txn_123");
        CreatePayment createPayment = this.setUp();
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertEquals("mpesa_txn_123", output.response());
    }
}

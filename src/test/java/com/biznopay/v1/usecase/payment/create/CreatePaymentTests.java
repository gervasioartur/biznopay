package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.domain.repository.PaymentRepository;
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

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetries() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(Mocks.maximumRetriesPaymentMock()));
        CreatePayment createPayment = new CreatePayment(paymentRepository);
        Assertions.assertThrows(ServiceUnavailableException.class, () -> createPayment.execute(input));
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsCompleteAndItExistsOnFindByIdempotencyKey() {
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Payment payment =  Mocks.completedPaymentMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = new CreatePayment(paymentRepository);
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output.response());
        Assertions.assertEquals(payment.getProviderPaymentId().get(), output.response());
    }

    @Test
    public void ShouldReturnAPaymentIfPaymentIsFailedAndItExistsOnFindByIdempotencyKey(){
        CreatePaymentInput input = Mocks.createPaymentInputMock();
        Payment payment =  Mocks.failedPaymentMock();
        Mockito.when(paymentRepository.findByIdempotencyKey(input.idempotencyKey())).thenReturn(Optional.of(payment));
        CreatePayment createPayment = new CreatePayment(paymentRepository);
        CreatePaymentOutput output = createPayment.execute(input);
        Assertions.assertNotNull(output);
        Assertions.assertEquals(payment.getStatus().name(), output.response());
    }
}

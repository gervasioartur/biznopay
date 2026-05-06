package com.biznopay.v1.usecase.payment.find;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.exception.ResourceNotFoundException;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.infra.persistence.jpa.repository.PaymentJpaRepository;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class FindPymentByIdTests {

    @Mock
    private PaymentGateway paymentGateway;

    private FindPymentById setup(){
        return new FindPymentById(paymentGateway);
    };

    @Test
    public void ShouldThrowResourceNotFoundExceptionWhenPaymentNotFound() {
        UUID paymentId = UUID.randomUUID();
        Mockito.when(paymentGateway.findById(paymentId)).thenReturn(Optional.empty());
        FindPymentById findPymentById = setup();
        Assertions.assertThrows(ResourceNotFoundException.class, () -> findPymentById.execute(paymentId));
    }

    @Test
    public void ShouldReturnPaymentIfExists(){
        Payment payment = Mocks.completedMpesaPaymentMock();
        UUID paymentId = payment.getId().value();
        Mockito.when(paymentGateway.findById(paymentId)).thenReturn(Optional.of(payment));
        FindPymentById findPymentById = setup();
        PaymentResponse paymentResponse = findPymentById.execute(paymentId);
        Assertions.assertNotNull(paymentResponse);
        Assertions.assertEquals(paymentId, paymentResponse.id());
    }
}

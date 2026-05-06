package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.infra.model.mapper.PaymentMapper;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentJpaEntity;
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
public class PaymentGatewayImplTests {
    @Mock
    private PaymentJpaRepository paymentJpaRepository;

    private PaymentGateway setUp() {
        return new PaymentGatewayImpl(paymentJpaRepository);
    }

    @Test
    public void ShouldReturnPaymentIfExistsOnFindByIdempotencyKey() {
        PaymentJpaEntity paymentJpaEntity = PaymentMapper.toJpaEntity(Mocks.completedMpesaPaymentMock());
        String idempotencyKey = paymentJpaEntity.getIdempotencyKey();
        Mockito.when(this.paymentJpaRepository.findByIdempotencyKey(idempotencyKey)).thenReturn(Optional.of(paymentJpaEntity));
        PaymentGateway paymentGateway = this.setUp();

        Payment payment = paymentGateway.findByIdempotencyKey(idempotencyKey).get();
        Assertions.assertEquals(paymentJpaEntity.getId(), payment.getId().value());
        Assertions.assertEquals(paymentJpaEntity.getIdempotencyKey(), payment.getIdempotencyKey());
        Assertions.assertEquals(paymentJpaEntity.getAmountInCents(), payment.getAmountInCents());
        Assertions.assertEquals(paymentJpaEntity.getCurrency(), payment.getCurrency());
        Assertions.assertEquals(paymentJpaEntity.getDescription(), payment.getDescription());
        Assertions.assertEquals(paymentJpaEntity.getStatus(), payment.getStatus());
        Assertions.assertEquals(paymentJpaEntity.getPaymentMethodDetails().getPhoneNumber(), payment.getPaymentMethodDetails().getPhoneNumber());
        Assertions.assertEquals(paymentJpaEntity.getProviderPaymentId(), payment.getProviderPaymentId().orElse(null));
        Assertions.assertEquals(paymentJpaEntity.getFailureReason(), payment.getFailureReason().orElse(null));
        Assertions.assertEquals(paymentJpaEntity.getRetryCount(), payment.getRetryCount());
        Assertions.assertEquals(paymentJpaEntity.getCreatedAt(), payment.getCreatedAt());
        Assertions.assertEquals(paymentJpaEntity.getUpdatedAt(), payment.getUpdatedAt());
    }

    @Test
    public void ShouldSavedPaymentAndReturnSavedPaymentOnSave() {
        Payment payment = Mocks.completedMpesaPaymentMock();
        PaymentJpaEntity paymentJpaEntity = PaymentMapper.toJpaEntity(payment);
        Mockito.when(this.paymentJpaRepository.save(Mockito.any())).thenReturn(paymentJpaEntity);
        PaymentGateway paymentGateway = this.setUp();

        Payment savedPayment = paymentGateway.save(payment);

        Assertions.assertEquals(paymentJpaEntity.getId(), savedPayment.getId().value());
        Assertions.assertEquals(paymentJpaEntity.getIdempotencyKey(), savedPayment.getIdempotencyKey());
        Assertions.assertEquals(paymentJpaEntity.getAmountInCents(), savedPayment.getAmountInCents());
        Assertions.assertEquals(paymentJpaEntity.getCurrency(), savedPayment.getCurrency());
        Assertions.assertEquals(paymentJpaEntity.getDescription(), savedPayment.getDescription());
        Assertions.assertEquals(paymentJpaEntity.getStatus(), savedPayment.getStatus());
        Assertions.assertEquals(paymentJpaEntity.getPaymentMethodDetails().getPhoneNumber(), savedPayment.getPaymentMethodDetails().getPhoneNumber());
        Assertions.assertEquals(paymentJpaEntity.getProviderPaymentId(), savedPayment.getProviderPaymentId().orElse(null));
        Assertions.assertEquals(paymentJpaEntity.getFailureReason(), savedPayment.getFailureReason().orElse(null));
        Assertions.assertEquals(paymentJpaEntity.getRetryCount(), savedPayment.getRetryCount());
        Assertions.assertEquals(paymentJpaEntity.getCreatedAt(), savedPayment.getCreatedAt());
        Assertions.assertEquals(paymentJpaEntity.getUpdatedAt(), savedPayment.getUpdatedAt());
    }

    @Test
    public void ShouldReturnOptionalOfEmptyIfPaymentDoesNotExistsOnFindById(){
        UUID paymentId  = UUID.randomUUID();
//        Mockito.when(this.paymentJpaRepository.findById(paymentId)).thenReturn(Optional.empty());
        PaymentGateway paymentGateway = this.setUp();
        Optional<Payment> payment = paymentGateway.findById(paymentId);
        Assertions.assertFalse(payment.isPresent());
    }
}

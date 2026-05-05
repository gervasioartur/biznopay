package com.biznopay.v1.infra.mapper;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentJpaEntity;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PaymentMapperTests {
    @Test
    public void ShouldReturnPendingMpesaDomainEntity() {
        PaymentMapper paymentMapper = new PaymentMapper();
        Payment pendingPaymentMock = Mocks.pendingMpesaPaymentMock();
        PaymentJpaEntity paymentJpaEntity = Mocks.paymentJpaEntityMock(pendingPaymentMock, pendingPaymentMock.getPaymentMethodDetails());

        Payment payment = PaymentMapper.toDomainEntity(paymentJpaEntity);

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
    public void ShouldReturnFailedMkeshDomainEntity() {
        Payment pendingPaymentMock = Mocks.failedMkeshPaymentMock();
        PaymentJpaEntity paymentJpaEntity = Mocks.paymentJpaEntityMock(pendingPaymentMock, pendingPaymentMock.getPaymentMethodDetails());

        Payment payment = PaymentMapper.toDomainEntity(paymentJpaEntity);

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
    public void ShouldReturnPendingEmolaDomainEntity() {
        Payment pendingPaymentMock = Mocks.pendingEmolaPaymentMock();
        PaymentJpaEntity paymentJpaEntity = Mocks.paymentJpaEntityMock(pendingPaymentMock, pendingPaymentMock.getPaymentMethodDetails());

        Payment payment = PaymentMapper.toDomainEntity(paymentJpaEntity);

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
}

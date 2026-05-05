package com.biznopay.v1.infra.mapper;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.entity.paymentMethodDetails.EmolaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.MpesaPaymentDetails;
import com.biznopay.v1.domain.entity.paymentMethodDetails.PaymentMethodDetails;
import com.biznopay.v1.domain.enums.PaymentMethodType;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentJpaEntity;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentMethodDetailsJpaEntity;

import java.util.Optional;

public class PaymentMapper {
    public static Payment toDomainEntity(PaymentJpaEntity paymentJpaEntity) {
        PaymentMethodDetails paymentMethodDetails = getDomainPaymentMethodType(paymentJpaEntity.getPaymentMethodDetails().getType(),
                paymentJpaEntity.getPaymentMethodDetails().getPhoneNumber());
        Optional<String> providerPaymentId = paymentJpaEntity.getProviderPaymentId() == null ? Optional.empty() : Optional.of(paymentJpaEntity.getProviderPaymentId());
        Optional<String> failureReason =  paymentJpaEntity.getFailureReason() ==  null ? Optional.empty() : Optional.of(paymentJpaEntity.getFailureReason());

        return Payment.with(paymentJpaEntity.getId(), paymentJpaEntity.getIdempotencyKey(), paymentJpaEntity.getAmountInCents(),
                paymentJpaEntity.getCurrency(), paymentJpaEntity.getDescription(), paymentJpaEntity.getStatus(),
                paymentMethodDetails, providerPaymentId, failureReason, paymentJpaEntity.getRetryCount(),
                paymentJpaEntity.getCreatedAt(), paymentJpaEntity.getUpdatedAt());
    }

    public static PaymentMethodDetails getDomainPaymentMethodType(PaymentMethodType type, String phoneNumber) {
        if (type == PaymentMethodType.MPESA)
            return MpesaPaymentDetails.create(phoneNumber);
        if (type == PaymentMethodType.EMOLA)
            return EmolaPaymentDetails.create(phoneNumber);
        if (type == PaymentMethodType.MKESH)
            return MpesaPaymentDetails.create(phoneNumber);
        return null;
    }

    public static PaymentMethodDetailsJpaEntity getJpaPaymentMethodType(PaymentMethodType type, String phoneNumber) {
        return new PaymentMethodDetailsJpaEntity(type, phoneNumber);
    }

    public static PaymentJpaEntity toJpaEntity(Payment payment) {
        PaymentMethodDetailsJpaEntity paymentMethodDetailsJpaEntity = getJpaPaymentMethodType(payment.getPaymentMethodDetails().getType(), payment.getPaymentMethodDetails().getPhoneNumber());
        return new PaymentJpaEntity(payment.getId().value(), payment.getIdempotencyKey(), payment.getAmountInCents(),
                payment.getCurrency(), payment.getDescription(), payment.getStatus(), paymentMethodDetailsJpaEntity,
                payment.getProviderPaymentId().orElse(null), payment.getFailureReason().orElse(null), payment.getRetryCount(),
                payment.getCreatedAt(), payment.getUpdatedAt());
    }
}

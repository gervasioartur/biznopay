package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.entity.payment.Payment;
import com.biznopay.v1.domain.gateway.PaymentGateway;
import com.biznopay.v1.infra.mapper.PaymentMapper;
import com.biznopay.v1.infra.persistence.jpa.entity.PaymentJpaEntity;
import com.biznopay.v1.infra.persistence.jpa.repository.PaymentJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentGatewayImpl implements PaymentGateway {
    private final PaymentJpaRepository paymentJpaRepository;

    @Autowired
    public PaymentGatewayImpl(PaymentJpaRepository paymentJpaRepository) {
        this.paymentJpaRepository = paymentJpaRepository;
    }

    @Override
    public Optional<Payment> findByIdempotencyKey(String idempotencyKey) {
        Optional<PaymentJpaEntity> paymentJpaEntity = paymentJpaRepository.findByIdempotencyKey(idempotencyKey);
        return paymentJpaEntity.map(PaymentMapper::toDomainEntity);
    }

    @Override
    public Payment save(Payment payment) {
        PaymentJpaEntity paymentJpaEntity = PaymentMapper.toJpaEntity(payment);
        paymentJpaEntity = paymentJpaRepository.save(paymentJpaEntity);
        return PaymentMapper.toDomainEntity(paymentJpaEntity);
    }
}

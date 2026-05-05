package com.biznopay.v1.infra.persistence.jpa.entity;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDetailsJpaEntity {
    @Enumerated(EnumType.STRING)
    private PaymentMethodType type;
    private String phoneNumber;
}

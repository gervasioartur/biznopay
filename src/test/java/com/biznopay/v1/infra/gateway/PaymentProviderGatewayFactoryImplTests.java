package com.biznopay.v1.infra.gateway;

import com.biznopay.v1.domain.enums.PaymentMethodType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PaymentProviderGatewayFactoryImplTests {

    @Mock
    private MockMpesaProviderGatewayImpl mpesa;
    @Mock
    private MockEmolaProviderGatewayImpl emola;
    @Mock
    private MockMkeshProviderGatewayImpl mkesh;

    @Test
    public void ShouldReturnCorrectGateway() {
        PaymentProviderGatewayFactoryImpl factory = new PaymentProviderGatewayFactoryImpl(mpesa, emola, mkesh);
        Assertions.assertEquals(mpesa, factory.getProvider(PaymentMethodType.MPESA));
        Assertions.assertEquals(emola, factory.getProvider(PaymentMethodType.EMOLA));
        Assertions.assertEquals(mkesh, factory.getProvider(PaymentMethodType.MKESH));
    }
}

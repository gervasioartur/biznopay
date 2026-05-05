package com.biznopay.v1.usecase.payment.create;

import com.biznopay.v1.domain.exception.ServiceUnavailableException;
import com.biznopay.v1.mocks.Mocks;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CreatePaymentTests {

    @Test
    public void ShouldReturnServiceUnavailableAfterAllRetries() {
        CreatePaymentInput input =  Mocks.createPaymentInputMock();
        CreatePayment createPayment =  new CreatePayment();
        Assertions.assertThrows(ServiceUnavailableException.class,()-> createPayment.execute(input));
    }
}

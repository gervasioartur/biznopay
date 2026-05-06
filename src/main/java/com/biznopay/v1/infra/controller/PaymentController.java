package com.biznopay.v1.infra.controller;

import com.biznopay.v1.domain.vo.ApiResponse;
import com.biznopay.v1.infra.model.dto.CreatePaymentRequest;
import com.biznopay.v1.infra.model.mapper.PaymentMapper;
import com.biznopay.v1.infra.util.FuncUtils;
import com.biznopay.v1.usecase.payment.create.CreatePayment;
import com.biznopay.v1.usecase.payment.create.CreatePaymentInput;
import com.biznopay.v1.usecase.payment.create.CreatePaymentOutput;
import com.biznopay.v1.usecase.payment.find.FindPymentById;
import com.biznopay.v1.usecase.payment.find.PaymentResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication")
@RequestMapping("/api/v1/payments")
public class PaymentController {
    private final CreatePayment createPayment;
    private final FindPymentById findPymentById;

    @PostMapping
    public ResponseEntity<ApiResponse<Object>> create(@RequestBody @Valid CreatePaymentRequest request) {
        CreatePaymentInput createPaymentInput = PaymentMapper.createPaymentInput(request);
        CreatePaymentOutput output = createPayment.execute(createPaymentInput);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(FuncUtils.buildResponseBody(true, output, null));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<Object>> create(@PathVariable("id") UUID paymentId ) {
        PaymentResponse output = findPymentById.execute(paymentId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(FuncUtils.buildResponseBody(true, output, null));
    }}

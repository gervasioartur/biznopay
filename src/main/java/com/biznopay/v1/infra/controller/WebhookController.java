package com.biznopay.v1.infra.controller;

import com.biznopay.v1.domain.vo.ApiResponse;
import com.biznopay.v1.infra.model.dto.WebhookRequest;
import com.biznopay.v1.infra.util.FuncUtils;
import com.biznopay.v1.usecase.payment.webhook.HandleWebhook;
import com.biznopay.v1.usecase.payment.webhook.HandleWebhookInput;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Webhooks")
@RequiredArgsConstructor
@RequestMapping("/api/v1/webhooks")
public class WebhookController {
    private final HandleWebhook handleWebhook;

    @PostMapping("/provider")
    public ResponseEntity<ApiResponse<Object>> handle(
            @RequestHeader("X-Signature") String signature,
            @RequestBody @Valid WebhookRequest request) {

        HandleWebhookInput input = new HandleWebhookInput(
                request.paymentId(),
                request.status(),
                request.failureReason(),
                request.rawPayload(),
                signature
        );

        handleWebhook.execute(input);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(FuncUtils.buildResponseBody(true, null, null));
    }
}
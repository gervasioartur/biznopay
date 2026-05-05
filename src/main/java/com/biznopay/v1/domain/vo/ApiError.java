package com.biznopay.v1.domain.vo;

public record ApiError(
        String code,
        String message
) {
}

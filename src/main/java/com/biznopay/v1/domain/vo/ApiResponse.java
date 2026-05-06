package com.biznopay.v1.domain.vo;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Instant timestamp
) {
}

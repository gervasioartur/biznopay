package com.biznopay.v1.infra.util;

import com.biznopay.v1.domain.vo.ApiError;
import com.biznopay.v1.domain.vo.ApiResponse;

import java.time.Instant;

public class FuncUtils {
    public static ApiResponse<Object> buildResponseBody(boolean success, Object data, ApiError error) {
        return new ApiResponse<Object>(success, data, error, Instant.now());
    }
}
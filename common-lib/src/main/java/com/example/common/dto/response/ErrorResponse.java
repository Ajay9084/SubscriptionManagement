package com.example.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Canonical error response envelope used by all services.
 * Returned by GlobalExceptionHandler for every error path.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    /** HTTP reason phrase, e.g. "Not Found" */
    private String error;

    /** Application-level error code, e.g. "SUBSCRIPTION_NOT_FOUND" */
    private String errorCode;

    private String message;

    private String path;
}

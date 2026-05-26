package com.uzair.bfhl.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standardized DTO returned when the application encounters errors or validation exceptions.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response structure returned for failed requests")
public class ErrorResponse {

    @JsonProperty("is_success")
    @Schema(description = "Represents success state of the operation, always false in this context", example = "false")
    private boolean isSuccess;

    @Schema(description = "Brief summary of the error type", example = "Validation Failed")
    private String message;

    @Schema(description = "Detailed error message or specific fields causing error", example = "data array must not be null")
    private String details;
}

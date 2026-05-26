package com.uzair.bfhl.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object for BFHL Post requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for BFHL array processing")
public class BfhlRequestDto {

    @NotNull(message = "data array must not be null")
    @Schema(description = "Array of strings comprising numbers, alphabets, and special characters", 
            example = "[\"a\", \"1\", \"334\", \"4\", \"R\", \"$\"]")
    private List<String> data;
}

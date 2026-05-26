package com.uzair.bfhl.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Data Transfer Object representing the JSON response structure of the BFHL process.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload containing categorized fields and processed statistics")
public class BfhlResponseDto {

    @JsonProperty("is_success")
    @Schema(description = "Represents success state of the operation", example = "true")
    private boolean isSuccess;

    @JsonProperty("user_id")
    @Schema(description = "Format: full_name_ddmmyyyy", example = "uzair_khan_15022004")
    private String userId;

    @Schema(description = "Registered email address", example = "uzair@example.com")
    private String email;

    @JsonProperty("roll_number")
    @Schema(description = "Registered roll number", example = "RGPV2026IT001")
    private String rollNumber;

    @JsonProperty("odd_numbers")
    @Schema(description = "List of all filtered odd integers in string format", example = "[\"1\"]")
    private List<String> oddNumbers;

    @JsonProperty("even_numbers")
    @Schema(description = "List of all filtered even integers in string format", example = "[\"334\", \"4\"]")
    private List<String> evenNumbers;

    @Schema(description = "List of all filtered alphabetic elements converted to UPPERCASE", example = "[\"A\", \"R\"]")
    private List<String> alphabets;

    @JsonProperty("special_characters")
    @Schema(description = "List of all filtered non-alphanumeric elements", example = "[\"$\"]")
    private List<String> specialCharacters;

    @Schema(description = "Sum of all parsed numbers, returned as string", example = "339")
    private String sum;

    @JsonProperty("concat_string")
    @Schema(description = "Processed concatenated alphabet string, reversed and with alternating case", example = "Ra")
    private String concatString;
}

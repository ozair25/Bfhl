package com.uzair.bfhl.controller;

import com.uzair.bfhl.dto.BfhlRequestDto;
import com.uzair.bfhl.dto.BfhlResponseDto;
import com.uzair.bfhl.service.BfhlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * REST Controller exposing BFHL operational endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/bfhl")
@Validated
@RequiredArgsConstructor
@Tag(name = "BFHL Controller", description = "Endpoints for parsing mixed array data inputs and checking system operation codes")
@CrossOrigin(origins = "*") // Allow global access for frontend clients
public class BfhlController {

    private final BfhlService bfhlService;

    /**
     * POST /bfhl
     * Accepts a list of inputs and returns categorized groupings alongside custom metrics.
     *
     * @param request payload conforming to DTO constraints
     * @return 200 OK containing parsed categorizations
     */
    @Operation(summary = "Process and categorize data array", 
               description = "Accepts a list containing numbers, alphabets, and special characters. Filters and groups them into odd numbers, even numbers, alphabets (uppercase), special characters, calculates numeric sum, and computes reversed alternating caps.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully processed and categorized input data",
                     content = @Content(schema = @Schema(implementation = BfhlResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid or malformed JSON payload structure",
                     content = @Content(schema = @Schema(implementation = com.uzair.bfhl.exception.ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Exception encountered during execution",
                     content = @Content(schema = @Schema(implementation = com.uzair.bfhl.exception.ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<BfhlResponseDto> processData(@Valid @RequestBody BfhlRequestDto request) {
        log.info("REST request to process BFHL data array");
        BfhlResponseDto response = bfhlService.processData(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /bfhl
     * Returns a hardcoded status operation code (typical for checking endpoint accessibility in automated testing).
     *
     * @return 200 OK containing operation_code: 1
     */
    @Operation(summary = "Fetch operations code", 
               description = "Returns hardcoded operation code to confirm system health and routing compatibility.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved operation code",
                     content = @Content(schema = @Schema(example = "{\"operation_code\": 1}")))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getOperationCode() {
        log.info("REST request to fetch BFHL operation code");
        return ResponseEntity.ok(Collections.singletonMap("operation_code", 1));
    }
}

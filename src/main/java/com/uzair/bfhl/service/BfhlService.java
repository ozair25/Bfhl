package com.uzair.bfhl.service;

import com.uzair.bfhl.dto.BfhlRequestDto;
import com.uzair.bfhl.dto.BfhlResponseDto;

/**
 * Service interface for processing input arrays and returning categorized results.
 */
public interface BfhlService {

    /**
     * Processes input list of strings, categories elements, performs mathematical sums,
     * and performs custom string operations.
     *
     * @param request the request body containing input array.
     * @return the processed response.
     */
    BfhlResponseDto processData(BfhlRequestDto request);
}

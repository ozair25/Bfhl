package com.uzair.bfhl.service.impl;

import com.uzair.bfhl.dto.BfhlRequestDto;
import com.uzair.bfhl.dto.BfhlResponseDto;
import com.uzair.bfhl.service.BfhlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Production-ready implementation of the BfhlService interface.
 * Implements strict parsing, categorizing, and statistical transformation rules.
 */
@Slf4j
@Service
public class BfhlServiceImpl implements BfhlService {

    // Hardcoded profile values required by business specs
    private static final String USER_ID = "uzair_khan_15022004";
    private static final String EMAIL = "uzair@example.com";
    private static final String ROLL_NUMBER = "RGPV2026IT001";

    @Override
    public BfhlResponseDto processData(BfhlRequestDto request) {
        log.info("Received request to process data array");

        if (request == null || request.getData() == null) {
            log.warn("Null request or data array received. Returning empty successful response.");
            return BfhlResponseDto.builder()
                    .isSuccess(true)
                    .userId(USER_ID)
                    .email(EMAIL)
                    .rollNumber(ROLL_NUMBER)
                    .oddNumbers(Collections.emptyList())
                    .evenNumbers(Collections.emptyList())
                    .alphabets(Collections.emptyList())
                    .specialCharacters(Collections.emptyList())
                    .sum("0")
                    .concatString("")
                    .build();
        }

        List<String> inputData = request.getData();
        List<String> oddNumbers = new ArrayList<>();
        List<String> evenNumbers = new ArrayList<>();
        List<String> alphabets = new ArrayList<>();
        List<String> specialCharacters = new ArrayList<>();
        BigInteger sum = BigInteger.ZERO;
        
        StringBuilder alphabeticSequenceBuilder = new StringBuilder();

        for (String element : inputData) {
            if (element == null) {
                continue;
            }
            
            String trimmed = element.trim();
            if (trimmed.isEmpty()) {
                continue;
            }

            // 1. Identify Numbers (positive/negative integer support)
            if (trimmed.matches("^-?\\d+$")) {
                try {
                    BigInteger numberVal = new BigInteger(trimmed);
                    // Determine even or odd via bitwise check (LSB bit 0 = odd)
                    if (numberVal.testBit(0)) {
                        oddNumbers.add(trimmed);
                    } else {
                        evenNumbers.add(trimmed);
                    }
                    sum = sum.add(numberVal);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse number string '{}' even though it matched regex", trimmed, e);
                    specialCharacters.add(trimmed);
                }
            }
            // 2. Identify Alphabets (pure alphabetical string support)
            else if (trimmed.matches("^[a-zA-Z]+$")) {
                alphabets.add(trimmed.toUpperCase());
                alphabeticSequenceBuilder.append(trimmed);
            }
            // 3. Any non-alphanumeric values (or mixed strings) go to special characters
            else {
                specialCharacters.add(trimmed);
            }
        }

        // 4. Calculate custom concat_string
        String processedConcatString = computeConcatString(alphabeticSequenceBuilder.toString());

        log.info("Processing complete. Computed sum: {}, alphabets count: {}, special characters count: {}", 
                sum, alphabets.size(), specialCharacters.size());

        return BfhlResponseDto.builder()
                .isSuccess(true)
                .userId(USER_ID)
                .email(EMAIL)
                .rollNumber(ROLL_NUMBER)
                .oddNumbers(oddNumbers)
                .evenNumbers(evenNumbers)
                .alphabets(alphabets)
                .specialCharacters(specialCharacters)
                .sum(sum.toString())
                .concatString(processedConcatString)
                .build();
    }

    /**
     * Reverses the string and applies alternating case starting with an uppercase letter.
     *
     * @param source the combined alphabetic characters string
     * @return the transformed alternating caps string
     */
    private String computeConcatString(String source) {
        if (source == null || source.isEmpty()) {
            return "";
        }

        // Reverse the combined alphabetic characters
        String reversed = new StringBuilder(source).reverse().toString();
        StringBuilder result = new StringBuilder(reversed.length());

        // Apply alternating caps starting with uppercase
        for (int i = 0; i < reversed.length(); i++) {
            char c = reversed.charAt(i);
            if (i % 2 == 0) {
                result.append(Character.toUpperCase(c));
            } else {
                result.append(Character.toLowerCase(c));
            }
        }

        return result.toString();
    }
}

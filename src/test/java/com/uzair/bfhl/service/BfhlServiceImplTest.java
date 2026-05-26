package com.uzair.bfhl.service;

import com.uzair.bfhl.dto.BfhlRequestDto;
import com.uzair.bfhl.dto.BfhlResponseDto;
import com.uzair.bfhl.service.impl.BfhlServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BfhlServiceImplTest {

    private BfhlService bfhlService;

    @BeforeEach
    void setUp() {
        bfhlService = new BfhlServiceImpl();
    }

    @Test
    void testProcessData_MixedData() {
        List<String> data = Arrays.asList("a", "1", "334", "4", "R", "$");
        BfhlRequestDto request = new BfhlRequestDto(data);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals("uzair_khan_15022004", response.getUserId());
        assertEquals("uzair@example.com", response.getEmail());
        assertEquals("RGPV2026IT001", response.getRollNumber());
        
        // Odd numbers: "1"
        assertEquals(List.of("1"), response.getOddNumbers());
        // Even numbers: "334", "4"
        assertEquals(List.of("334", "4"), response.getEvenNumbers());
        // Alphabets (uppercase): "A", "R"
        assertEquals(List.of("A", "R"), response.getAlphabets());
        // Special characters: "$"
        assertEquals(List.of("$"), response.getSpecialCharacters());
        // Sum: 1 + 334 + 4 = 339
        assertEquals("339", response.getSum());
        // Concat string: "a", "R" -> "aR" -> reversed "Ra" -> alternate upper "Ra"
        assertEquals("Ra", response.getConcatString());
    }

    @Test
    void testProcessData_OnlyAlphabets() {
        List<String> data = Arrays.asList("A", "ABCD", "DOE");
        BfhlRequestDto request = new BfhlRequestDto(data);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertEquals(List.of("A", "ABCD", "DOE"), response.getAlphabets());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        // Concat: "A" + "ABCD" + "DOE" -> "AABCDDOE" -> reversed "EODDCBAA" -> alternating "EoDdCbAa"
        assertEquals("EoDdCbAa", response.getConcatString());
    }

    @Test
    void testProcessData_OnlyNumbers() {
        List<String> data = Arrays.asList("12", "15", "100", "-5");
        BfhlRequestDto request = new BfhlRequestDto(data);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(Arrays.asList("15", "-5"), response.getOddNumbers());
        assertEquals(Arrays.asList("12", "100"), response.getEvenNumbers());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        // Sum: 12 + 15 + 100 - 5 = 122
        assertEquals("122", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_OnlySpecialCharacters() {
        List<String> data = Arrays.asList("$", "@", "#", "!", " ");
        BfhlRequestDto request = new BfhlRequestDto(data);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        // Note: spaces or empty items are ignored, but actual special chars are retained
        assertEquals(Arrays.asList("$", "@", "#", "!"), response.getSpecialCharacters());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_EmptyInput() {
        BfhlRequestDto request = new BfhlRequestDto(Collections.emptyList());

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_NullInput() {
        BfhlRequestDto request = new BfhlRequestDto(null);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_NullRequest() {
        BfhlResponseDto response = bfhlService.processData(null);

        assertTrue(response.isSuccess());
        assertTrue(response.getOddNumbers().isEmpty());
        assertTrue(response.getEvenNumbers().isEmpty());
        assertTrue(response.getAlphabets().isEmpty());
        assertTrue(response.getSpecialCharacters().isEmpty());
        assertEquals("0", response.getSum());
        assertEquals("", response.getConcatString());
    }

    @Test
    void testProcessData_LargeNumbersPreventOverflow() {
        // Test values that would exceed standard Integer/Long capacities to prove safe BigInteger parsing
        String largeOdd = "9999999999999999999999999999999999999991";
        String largeEven = "10000000000000000000000000000000000000000";
        List<String> data = Arrays.asList(largeOdd, largeEven);
        BfhlRequestDto request = new BfhlRequestDto(data);

        BfhlResponseDto response = bfhlService.processData(request);

        assertTrue(response.isSuccess());
        assertEquals(List.of(largeOdd), response.getOddNumbers());
        assertEquals(List.of(largeEven), response.getEvenNumbers());
        // Sum check: 9999999999999999999999999999999999999991 + 10000000000000000000000000000000000000000 = 19999999999999999999999999999999999999991
        assertEquals("19999999999999999999999999999999999999991", response.getSum());
    }
}

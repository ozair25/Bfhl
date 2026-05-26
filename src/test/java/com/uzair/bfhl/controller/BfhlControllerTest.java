package com.uzair.bfhl.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uzair.bfhl.dto.BfhlRequestDto;
import com.uzair.bfhl.dto.BfhlResponseDto;
import com.uzair.bfhl.exception.InvalidInputException;
import com.uzair.bfhl.service.BfhlService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BfhlController.class)
class BfhlControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BfhlService bfhlService;

    @Test
    void testGetOperationCode_ReturnsSuccess() throws Exception {
        mockMvc.perform(get("/bfhl"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.operation_code", is(1)));
    }

    @Test
    void testProcessData_SuccessResponse() throws Exception {
        BfhlRequestDto request = new BfhlRequestDto(Arrays.asList("a", "1"));
        
        BfhlResponseDto mockResponse = BfhlResponseDto.builder()
                .isSuccess(true)
                .userId("uzair_khan_15022004")
                .email("uzair@example.com")
                .rollNumber("RGPV2026IT001")
                .oddNumbers(Collections.singletonList("1"))
                .evenNumbers(Collections.emptyList())
                .alphabets(Collections.singletonList("A"))
                .specialCharacters(Collections.emptyList())
                .sum("1")
                .concatString("A")
                .build();

        Mockito.when(bfhlService.processData(any(BfhlRequestDto.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.is_success", is(true)))
                .andExpect(jsonPath("$.user_id", is("uzair_khan_15022004")))
                .andExpect(jsonPath("$.email", is("uzair@example.com")))
                .andExpect(jsonPath("$.roll_number", is("RGPV2026IT001")))
                .andExpect(jsonPath("$.odd_numbers[0]", is("1")))
                .andExpect(jsonPath("$.alphabets[0]", is("A")))
                .andExpect(jsonPath("$.sum", is("1")))
                .andExpect(jsonPath("$.concat_string", is("A")));
    }

    @Test
    void testProcessData_NullDataArray_ReturnsValidationError() throws Exception {
        BfhlRequestDto request = new BfhlRequestDto(null);

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.message", is("Validation Failed")));
    }

    @Test
    void testProcessData_MalformedJson_ReturnsBadRequest() throws Exception {
        String malformedJson = "{ \"data\": [ \"a\", ";

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.message", is("Malformed Request Body")));
    }

    @Test
    void testProcessData_BusinessValidationFails_ReturnsBadRequest() throws Exception {
        BfhlRequestDto request = new BfhlRequestDto(Collections.singletonList("error"));

        Mockito.when(bfhlService.processData(any(BfhlRequestDto.class)))
                .thenThrow(new InvalidInputException("Custom validation failed"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.message", is("Invalid Input")))
                .andExpect(jsonPath("$.details", is("Custom validation failed")));
    }

    @Test
    void testProcessData_UnexpectedError_ReturnsInternalServerError() throws Exception {
        BfhlRequestDto request = new BfhlRequestDto(Collections.singletonList("error"));

        Mockito.when(bfhlService.processData(any(BfhlRequestDto.class)))
                .thenThrow(new RuntimeException("Database down or similar crash"));

        mockMvc.perform(post("/bfhl")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.is_success", is(false)))
                .andExpect(jsonPath("$.message", is("Internal Server Error")))
                .andExpect(jsonPath("$.details", is("An unexpected error occurred. Please contact the administrator.")));
    }
}

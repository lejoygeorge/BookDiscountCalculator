package com.bookorder.discountcalculator.controller;

import com.bookorder.discountcalculator.model.BillDetails;
import com.bookorder.discountcalculator.model.BookItem;
import com.bookorder.discountcalculator.service.PriceCalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceCalculatorService service;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/orders/price";

    @Test
    @DisplayName("Positive: Should return HTTP 200 and calculated bill details for a valid request")
    void getOrder_Success_ReturnsBillDetails() throws Exception {
        BookItem mockBook = new BookItem();
        mockBook.setBook(BookItem.BookEnum.CLEAN_CODE);
        mockBook.setQuantity(2);
        List<BookItem> requestBody = List.of(mockBook);
        BillDetails mockResponse = new BillDetails();
        mockResponse.setFinalPrice(80.0);
        mockResponse.setDiscount(20.0);
        Mockito.when(service.calculatePrice(anyList())).thenReturn(mockResponse);
        String jsonRequest = objectMapper.writeValueAsString(requestBody);
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPrice").value(80.0))
                .andExpect(jsonPath("$.discount").value(20.0));
    }

    @Test
    @DisplayName("Negative: Should return HTTP 400 Bad Request when request body is completely missing")
    void getOrder_Failure_MissingBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(service, Mockito.never()).calculatePrice(anyList());
    }

    @Test
    @DisplayName("Should return 400 Bad Request when an invalid Enum value is provided")
    void whenInvalidEnumProvided_thenReturns400BadRequest() throws Exception {
        String requestBodyWithInvalidEnum = """
                [
                    {
                        "book": "BAD_CODE",
                        "quantity": 2
                    }
                ]
                """;

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBodyWithInvalidEnum))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Invalid value 'BAD_CODE'")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when the book basket list is empty")
    void whenListIsEmpty_thenReturns400BadRequest() throws Exception {
        String emptyListRequest = "[]";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(emptyListRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Book basket cannot be empty")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when quantity is less than zero")
    void whenQuantityIsNegative_thenReturns400BadRequest() throws Exception {
        String invalidQuantityRequest = """
                [
                    {
                        "book": "CLEAN_CODE",
                        "quantity": -5
                    }
                ]
                """;

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidQuantityRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("greater than or equal to 0")));
    }

    @Test
    @DisplayName("Should return 400 Bad Request when JSON is malformed (e.g. missing bracket)")
    void whenMalformedJson_thenReturns400BadRequest() throws Exception {
        String malformedJson = "[{\"book\": \"CLEAN_CODE\"";

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Malformed JSON request")));
    }
}


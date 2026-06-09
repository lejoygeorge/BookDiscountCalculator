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

import static org.mockito.ArgumentMatchers.anyList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PriceCalculatorService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Positive: Should return HTTP 200 and calculated bill details for a valid request")
    void getOrder_Success_ReturnsBillDetails() throws Exception {
        BookItem mockBook = new BookItem();
        List<BookItem> requestBody = List.of(mockBook);
        BillDetails mockResponse = new BillDetails();
        mockResponse.setFinalPrice(80.0);
        mockResponse.setDiscount(20.0);
        Mockito.when(service.calculatePrice(anyList())).thenReturn(mockResponse);
        String jsonRequest = objectMapper.writeValueAsString(requestBody);
        mockMvc.perform(get("/api/orders/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.finalPrice").value(80.0))
                .andExpect(jsonPath("$.discount").value(20.0));
    }

    @Test
    @DisplayName("Negative: Should return HTTP 400 Bad Request when request body is completely missing")
    void getOrder_Failure_MissingBodyReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/orders/book")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        Mockito.verify(service, Mockito.never()).calculatePrice(anyList());
    }
}

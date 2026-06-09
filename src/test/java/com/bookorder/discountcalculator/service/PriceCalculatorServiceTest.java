package com.bookorder.discountcalculator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PriceCalculatorServiceTest {

    private PriceCalculatorService priceCalculatorService;

    @BeforeEach
    public void setup() {
        priceCalculatorService = new PriceCalculatorService();
    }

    @Test
    @DisplayName("Should return 0.0 for all prices when the basket is empty")
    void testCalculatePrice_EmptyBasket() {
        double receipt = priceCalculatorService.calculatePrice(Collections.emptyList());
        assertEquals(0.0, receipt, 0.001);
    }
}
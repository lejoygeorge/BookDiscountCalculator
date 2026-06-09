package com.bookorder.discountcalculator.service;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

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

    @Test
    @DisplayName("Should return standard price with zero discount for a single item")
    void testCalculatePrice_SingleItem() {
        double receipt = priceCalculatorService.calculatePrice(Arrays.asList(new Integer[]{1}));
        assertEquals(50.0, receipt, 0.001);
    }
}
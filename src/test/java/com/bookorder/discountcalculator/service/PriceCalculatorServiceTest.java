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
        double receipt = priceCalculatorService.calculatePrice(List.of(0, 0, 0, 0, 0));
        assertEquals(0.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return standard price with zero discount for a single item")
    void testCalculatePrice_SingleItem() {
        double receipt = priceCalculatorService.calculatePrice(List.of(1, 0, 0, 0, 0));
        assertEquals(50.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return full price (100.0) for two copies of the same book with no discount")
    void testTwoSameBooks_NoDiscount() {
        double receipt = priceCalculatorService.calculatePrice(List.of(2, 0, 0, 0, 0));
        assertEquals(100.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return full price (95.0) for two copies of distinct book with 5% discount")
    void testTwoDistinctBooks_5PresentDiscount() {
        double receipt = priceCalculatorService.calculatePrice(List.of(1, 1, 0, 0, 0));
        assertEquals(95.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return full price (135.0) for three copies of distinct book with 10% discount")
    void testThreeDistinctBooks_10PresentDiscount() {
        double receipt = priceCalculatorService.calculatePrice(List.of(1, 1, 1, 0, 0));
        assertEquals(135.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return full price (160.0) for three copies of distinct book with 20% discount")
    void testFourDistinctBooks_20PresentDiscount() {
        double receipt = priceCalculatorService.calculatePrice(List.of(1, 1, 1, 1, 0));
        assertEquals(160.0, receipt, 0.001);
    }

}
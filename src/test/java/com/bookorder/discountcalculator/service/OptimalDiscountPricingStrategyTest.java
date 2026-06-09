package com.bookorder.discountcalculator.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OptimalDiscountPricingStrategyTest {

    private OptimalDiscountPricingStrategy pricingStrategy;

    @BeforeEach
    void setup() {
        pricingStrategy = new OptimalDiscountPricingStrategy();
    }

    @Test
    @DisplayName("Should return 0.0 for all prices when the basket is empty")
    void testCalculatePrice_EmptyBasket() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(0, 0, 0, 0, 0));
        assertEquals(0.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return standard price with zero discount for a single item")
    void testCalculatePrice_SingleItem() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(1, 0, 0, 0, 0));
        assertEquals(50.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return full price (100.0) for two copies of the same book with no discount")
    void testTwoSameBooks_NoDiscount() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(2, 0, 0, 0, 0));
        assertEquals(100.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return final price (95.0) for two copies of distinct book with 5% discount")
    void testTwoDistinctBooks_5PresentDiscount() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(1, 1, 0, 0, 0));
        assertEquals(95.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return final price (135.0) for three copies of distinct book with 10% discount")
    void testThreeDistinctBooks_10PresentDiscount() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(1, 1, 1, 0, 0));
        assertEquals(135.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return final price (160.0) for four copies of distinct book with 20% discount")
    void testFourDistinctBooks_20PresentDiscount() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(1, 1, 1, 1, 0));
        assertEquals(160.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Should return final price (187.5) for five copies of distinct book with 25% discount")
    void testFiveDistinctBooks_25PresentDiscount() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(1, 1, 1, 1, 1));
        assertEquals(187.5, receipt, 0.001);
    }

    @Test
    @DisplayName("Should calculate the correct price for a mix of grouped and ungrouped identical books")
    void testMixedBooks_SimpleGrouping() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(2, 1, 0, 0, 0));
        assertEquals(145.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Edge Case: Should prefer two groups of four (320.0) over a group of five and three (322.5)")
    void testComplexEdgeCase_FourPlusFourIsCheaperThanFivePlusThree() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(2, 2, 2, 1, 1));
        assertEquals(320.0, receipt, 0.001);
    }

    @Test
    @DisplayName("Performance: Should quickly calculate the minimum price for a large basket without timing out")
    void testLargeBasket() {
        double receipt = pricingStrategy.calculateMinimumPrice(List.of(5, 5, 5, 5, 5));
        assertEquals(937.5, receipt, 0.001);
    }

}
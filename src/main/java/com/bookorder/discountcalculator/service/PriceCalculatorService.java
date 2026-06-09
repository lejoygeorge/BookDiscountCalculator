package com.bookorder.discountcalculator.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceCalculatorService {

    private final PricingStrategy pricingStrategy;

    public PriceCalculatorService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public double calculatePrice(List<Integer> bookCounts) {
        double finalPrice =0.0;
        finalPrice = pricingStrategy.calculateMinimumPrice(bookCounts);
        return finalPrice;
    }
}

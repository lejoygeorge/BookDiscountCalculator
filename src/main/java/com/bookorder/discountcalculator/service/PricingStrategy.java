package com.bookorder.discountcalculator.service;

import java.util.List;

public interface PricingStrategy {
    double calculateMinimumPrice(List<Integer> bookQuantities);
}
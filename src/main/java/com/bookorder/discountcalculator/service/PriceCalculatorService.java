package com.bookorder.discountcalculator.service;

import java.util.List;

public class PriceCalculatorService {
    public static final double BOOK_PRICE = 50.0;
    public double calculatePrice(List<Object> objects) {
        double finalPrice =0.0;
        if(!objects.isEmpty()) {
            if (objects.size() == 1) {
                finalPrice = BOOK_PRICE;
            }
        }
        return finalPrice;
    }
}

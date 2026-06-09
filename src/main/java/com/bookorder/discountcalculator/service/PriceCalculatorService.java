package com.bookorder.discountcalculator.service;

import java.util.List;

public class PriceCalculatorService {
    public static final double BOOK_PRICE = 50.0;
    public double calculatePrice(List<Integer> objects) {
        double finalPrice =0.0;
        for(Integer count: objects){
            finalPrice += count * BOOK_PRICE;
        }
        return finalPrice;
    }
}

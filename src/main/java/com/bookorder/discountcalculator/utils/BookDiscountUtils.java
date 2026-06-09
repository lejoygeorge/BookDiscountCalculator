package com.bookorder.discountcalculator.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BookDiscountUtils {
    public static final double BOOK_PRICE = 50.0;
    public static final Map<Integer, Double> GROUP_PRICE;

    private BookDiscountUtils() {
        throw new IllegalStateException("Utility class");
    }

    static{
        Map<Integer, Double> tempMap = new HashMap<>();
        Map<Integer, Integer> discount = Map.of(1,0, 2,5,3,10,4,20,5,25);
        discount.forEach((groupSize, discountPercentage)-> {
            double discountedPrice = groupSize * BOOK_PRICE * (100 - discountPercentage) / 100.0;
            tempMap.put( groupSize, discountedPrice);
        });
        GROUP_PRICE = Collections.unmodifiableMap(tempMap);
    }
}

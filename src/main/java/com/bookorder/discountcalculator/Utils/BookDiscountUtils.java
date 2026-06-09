package com.bookorder.discountcalculator.Utils;

import java.util.HashMap;
import java.util.Map;

public class BookDiscountUtils {
    public static final double BOOK_PRICE = 50.0;
    public static final Map<Integer, Double> GROUP_PRICE = new HashMap<>();

    private BookDiscountUtils() {
        throw new IllegalStateException("Utility class");
    }

    static{
        Map<Integer, Integer> discount = Map.of(1,0, 2,5,3,10,4,20,5,25);
        discount.forEach((groupSize, discountPercentage)-> {
            double discountedPrice = groupSize * BOOK_PRICE * (100 - discountPercentage) / 100.0;
            GROUP_PRICE.put( groupSize, discountedPrice);
        });
    }
}

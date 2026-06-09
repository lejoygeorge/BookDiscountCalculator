package com.bookorder.discountcalculator.service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

public class PriceCalculatorService {
    public static final double BOOK_PRICE = 50.0;
    protected static final Map<Integer, Double> GROUP_PRICE = new HashMap<>();
    private final Map<BookBasket, Double> basketCache = new ConcurrentHashMap<>();

    static{
        GROUP_PRICE.put( 1, 50.0);
        GROUP_PRICE.put( 2, 95.0);
        GROUP_PRICE.put( 3, 135.0);
    }
    public double calculatePrice(List<Integer> bookCounts) {
        double finalPrice =0.0;
        finalPrice = minimumPrice(new BookBasket(bookCounts.toArray(new Integer[0])));
        return finalPrice;
    }

    public record BookBasket(Integer[] counts) {
        public BookBasket{
            counts = counts.clone();
            Arrays.sort(counts, Collections.reverseOrder());

        }

        public boolean isEmpty(){
            return Arrays.stream(counts).allMatch(count ->count ==0 );
        }

        @Override
        public boolean equals(Object obj){
            return obj instanceof BookBasket other && Arrays.equals(counts, other.counts);
        }

        @Override
        public int hashCode(){
            return Arrays.hashCode(counts);
        }
    }

    private double minimumPrice(BookBasket currentBasket) {
        if (currentBasket.isEmpty()) {
            return 0.0;
        }
        if (basketCache.containsKey(currentBasket)) {
            return basketCache.get(currentBasket);
        }
        int[] availableBooks = IntStream.range(0, currentBasket.counts().length)
                .filter(index -> currentBasket.counts()[index] > 0)
                .toArray();
        double minCost = Double.MAX_VALUE;
        int totalSubsets = 1 << availableBooks.length;
        for (int mask = 1; mask < totalSubsets; mask++) {
            Integer[] nextCounts = currentBasket.counts().clone();
            int groupSize = 0;

            for (int bit = 0; bit < availableBooks.length; bit++) {
                if ((mask & (1 << bit)) != 0) {
                    nextCounts[availableBooks[bit]]--;
                    groupSize++;
                }
            }
            double cost = GROUP_PRICE.get(groupSize) + minimumPrice(new BookBasket(nextCounts));
            minCost = Math.min(minCost, cost);
        }
        basketCache.put(currentBasket, minCost);
        return minCost;
    }
}

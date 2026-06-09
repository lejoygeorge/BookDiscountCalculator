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
        GROUP_PRICE.put( 4, 160.0);
        GROUP_PRICE.put( 5, 187.5);
    }
    public double calculatePrice(List<Integer> bookCounts) {
        double finalPrice =0.0;
        finalPrice = findMinimumPrice(new BookBasket(bookCounts));
        return finalPrice;
    }

    public record BookBasket(List<Integer> counts) {
        public boolean isEmpty() {
            return counts.stream().allMatch(count -> count == 0);
        }
    }

    private double findMinimumPrice(BookBasket currentBasket) {
        if (currentBasket.isEmpty()) {
            return 0.0;
        }
        Double cachedPrice = basketCache.get(currentBasket);
        if (cachedPrice != null) {
            return cachedPrice;
        }
        var counts = currentBasket.counts();
        int[] distinctBooksPresent = IntStream.range(0, counts.size())
                .filter(index -> counts.get(index) > 0).toArray();
        int numberOfPossibleCombinations = 1 << distinctBooksPresent.length;
        double minimumPrice = IntStream.range(1, numberOfPossibleCombinations)
                .mapToDouble(combination -> evaluateCombination(currentBasket, distinctBooksPresent, combination))
                .min().orElse(Double.MAX_VALUE);
        basketCache.put(currentBasket, minimumPrice);
        return minimumPrice;
    }

    private double evaluateCombination(BookBasket basket, int[] distinctBooksPresent, int combination) {
        var remainingBooks = new ArrayList<>(basket.counts());
        var activeIndices = IntStream.range(0, distinctBooksPresent.length)
                .filter(bookIndex -> (combination & (1 << bookIndex)) != 0)
                .mapToObj(bookIndex -> distinctBooksPresent[bookIndex]).toList();
        activeIndices.forEach(actualBookId -> remainingBooks.set(actualBookId, remainingBooks.get(actualBookId) - 1));
        int currentGroupSize = activeIndices.size();
        double groupPrice = GROUP_PRICE.get(currentGroupSize);
        return groupPrice + findMinimumPrice(new BookBasket(remainingBooks));
    }
}

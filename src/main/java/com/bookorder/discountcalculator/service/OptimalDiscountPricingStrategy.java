package com.bookorder.discountcalculator.service;

import com.bookorder.discountcalculator.model.BookBasket;
import com.bookorder.discountcalculator.Utils.BookDiscountUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.IntStream;

@Component
public class OptimalDiscountPricingStrategy implements PricingStrategy {

    private final Map<BookBasket, Double> basketCache = new ConcurrentHashMap<>();

    @Override
    public double calculateMinimumPrice(List<Integer> bookQuantities) {
        return findMinimumPrice(new BookBasket(bookQuantities));
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
        double groupPrice = BookDiscountUtils.GROUP_PRICE.get(currentGroupSize);
        return groupPrice + findMinimumPrice(new BookBasket(remainingBooks));
    }
}
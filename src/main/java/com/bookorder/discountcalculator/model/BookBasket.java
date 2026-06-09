package com.bookorder.discountcalculator.model;

import java.util.List;

public record BookBasket(List<Integer> counts) {
    public boolean isEmpty() {
        return counts.stream().allMatch(count -> count == 0);
    }
}
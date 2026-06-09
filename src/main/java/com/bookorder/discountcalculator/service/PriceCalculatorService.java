package com.bookorder.discountcalculator.service;

import com.bookorder.discountcalculator.model.BillDetails;
import com.bookorder.discountcalculator.model.BookItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceCalculatorService {

    private final PricingStrategy pricingStrategy;

    public BillDetails calculatePrice(List<BookItem> purchasedItems) {
        double totalOriginalPrice = purchasedItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getBook().getPrice()).sum();
        List<Integer> quantities = purchasedItems.stream()
                .collect(Collectors.groupingBy(BookItem::getBook,
                        () -> new EnumMap<>(com.bookorder.discountcalculator.model.BookItem.BookEnum.class),
                        Collectors.summingInt(BookItem::getQuantity))).values().stream().toList();
        double totalFinalPrice = pricingStrategy.calculateMinimumPrice(quantities);
        double totalDiscount = totalOriginalPrice - totalFinalPrice;
        var receipt = new BillDetails();
        receipt.setOriginalPrice(totalOriginalPrice);
        receipt.setFinalPrice(totalFinalPrice);
        receipt.setDiscount(totalDiscount);
        return receipt;
    }
}

package com.bookorder.discountcalculator.service;

import com.bookorder.discountcalculator.model.BillDetails;
import com.bookorder.discountcalculator.model.BookItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static com.bookorder.discountcalculator.model.BookItem.BookEnum.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PriceCalculatorServiceTest {

    @Mock
    private PricingStrategy pricingStrategy;

    @InjectMocks
    private PriceCalculatorService priceCalculatorService;

    @Test
    @DisplayName("Should return 0.0 for all prices when the basket is empty")
    void testCalculatePrice_EmptyBasket() {
        when(pricingStrategy.calculateMinimumPrice(anyList())).thenReturn(0.0);
        BillDetails receipt = priceCalculatorService.calculatePrice(Collections.emptyList());
        assertEquals(0.0, receipt.getOriginalPrice(), 0.001);
        assertEquals(0.0, receipt.getFinalPrice(), 0.001);
        assertEquals(0.0, receipt.getDiscount(), 0.001);
    }

    @Test
    @DisplayName("Should return standard price with zero discount for a single item")
    void testCalculatePrice_SingleItem() {
        BookItem book1 = new BookItem();
        book1.setBook(CLEAN_CODE);
        book1.setQuantity(1);
        List<BookItem> items = List.of(book1);
        when(pricingStrategy.calculateMinimumPrice(anyList())).thenReturn(50.0);
        BillDetails receipt = priceCalculatorService.calculatePrice(items);
        assertEquals(50.0, receipt.getOriginalPrice(), 0.001);
        assertEquals(50.0, receipt.getFinalPrice(), 0.001);
        assertEquals(0.0, receipt.getDiscount(), 0.001);
    }

    @Test
    @DisplayName("Should aggregate original prices and calculate discount based on the strategy's final price")
    void testCalculatePrice_MultipleItemsWithGrouping() {
        BookItem book1 = new BookItem();
        book1.setBook(CLEAN_CODE);
        book1.setQuantity(2);

        BookItem book2 = new BookItem();
        book2.setBook(THE_CLEAN_CODER);
        book2.setQuantity(1);

        BookItem book3 = new BookItem();
        book3.setBook(CLEAN_ARCHITECTURE);
        book3.setQuantity(1);

        List<BookItem> items = List.of(book1,book2,book3);
        when(pricingStrategy.calculateMinimumPrice(anyList())).thenReturn(180.0);
        BillDetails receipt = priceCalculatorService.calculatePrice(items);
        assertEquals(200.0, receipt.getOriginalPrice(), 0.001);
        assertEquals(180.0, receipt.getFinalPrice(), 0.001);
        assertEquals(20.0, receipt.getDiscount(), 0.001);
    }
}
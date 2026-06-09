package com.bookorder.discountcalculator.utils;

import com.bookorder.discountcalculator.Utils.BookDiscountUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookDiscountUtilsTest {

    @Test
    @DisplayName("Should correctly calculate and store discounted prices for group sizes 1 to 5")
    void testGroupPricesAreCalculatedCorrectly() {
        assertEquals(50.0, BookDiscountUtils.GROUP_PRICE.get(1), 0.001, "Price for 1 book should be 50.0");
        assertEquals(95.0, BookDiscountUtils.GROUP_PRICE.get(2), 0.001, "Price for 2 books should be 95.0");
        assertEquals(135.0, BookDiscountUtils.GROUP_PRICE.get(3), 0.001, "Price for 3 books should be 135.0");
        assertEquals(160.0, BookDiscountUtils.GROUP_PRICE.get(4), 0.001, "Price for 4 books should be 160.0");
        assertEquals(187.5, BookDiscountUtils.GROUP_PRICE.get(5), 0.001, "Price for 5 books should be 187.5");
    }

    @Test
    @DisplayName("Private constructor should throw an Exception to prevent instantiation")
    void testPrivateConstructorThrowsException() throws NoSuchMethodException {
        Constructor<BookDiscountUtils> constructor = BookDiscountUtils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}

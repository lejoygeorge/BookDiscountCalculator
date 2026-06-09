package com.bookorder.discountcalculator.controller;

import com.bookorder.discountcalculator.model.BillDetails;
import com.bookorder.discountcalculator.model.BookItem;
import com.bookorder.discountcalculator.service.PriceCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${discountcalculator.controller.path}")
public class OrderController {

    private final PriceCalculatorService service;

    @PostMapping("${discountcalculator.endpoint.getprice}")
    public BillDetails getDiscountPrice(@RequestBody List<BookItem> bookBasket) {
        return service.calculatePrice(bookBasket);
    }
}
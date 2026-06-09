package com.bookorder.discountcalculator.controller;

import com.bookorder.discountcalculator.model.BillDetails;
import com.bookorder.discountcalculator.model.BookItem;
import com.bookorder.discountcalculator.service.PriceCalculatorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${discountcalculator.controller.path}")
@Validated
public class OrderController {

    private final PriceCalculatorService service;

    @PostMapping("${discountcalculator.endpoint.getprice}")
    public BillDetails getDiscountPrice(
            @RequestBody
            @NotEmpty(message = "Book basket cannot be empty")
            List<@Valid BookItem> bookBasket) {
        return service.calculatePrice(bookBasket);
    }
}
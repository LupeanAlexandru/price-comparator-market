package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.DiscountDTO;
import com.example.price_comparator_market.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discounts")
@RequiredArgsConstructor
public class DiscountController {

    private final DiscountService discountService;

    /**
     * Retrieves all available discounts.
     *
     * @return a {@link ResponseEntity} containing the list of {@link DiscountDTO} objects
     * representing all discounts found in the system, with HTTP status 200 (OK)
     */
    @GetMapping
    public ResponseEntity<List<DiscountDTO>> getAllDiscounts() {
        return ResponseEntity.ok(discountService.getAllDiscounts());
    }

    /**
     * Retrieves a discount by its unique identifier.
     *
     * @param id the ID of the discount to retrieve
     * @return a {@link ResponseEntity} containing the {@link DiscountDTO} for the specified ID,
     * with HTTP status 200 (OK)
     * * @throws DiscountNotFoundException if no discount is found with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<DiscountDTO> getDiscountById(@PathVariable Long id) {
        return ResponseEntity.ok(discountService.getDiscountById(id));
    }
}
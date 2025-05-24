package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.BestDiscountDTO;
import com.example.price_comparator_market.dto.DiscountDTO;
import com.example.price_comparator_market.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    /**
     * Retrieves the best available discount for each product on the specified date.
     * <p>
     * If no date is provided in the "date" request header, the current date is used by default.
     * The best discount is defined as the highest percentage discount available for a given product
     * across all stores on the specified date.
     *
     * @param date the date to evaluate discounts against, passed as a request header in ISO format (yyyy-MM-dd);
     *             if null, the current date is used
     * @return a {@link ResponseEntity} containing a list of {@link BestDiscountDTO} objects representing
     *         the best discount per product
     */
    @GetMapping("/best")
    public ResponseEntity<List<BestDiscountDTO>> getBestDiscounts(
            @RequestHeader(value = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return ResponseEntity.ok(discountService.getBestDiscounts(date));
    }
}
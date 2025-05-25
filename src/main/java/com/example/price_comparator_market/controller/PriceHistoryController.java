package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.PriceHistoryResponseDTO;
import com.example.price_comparator_market.service.PriceHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/price-history")
@RequiredArgsConstructor
public class PriceHistoryController {

    private final PriceHistoryService priceHistoryService;

    /**
     * Retrieves the price history for a specified product, optionally filtered by store, brand, and category.
     * <p>
     * If a matching history is found, returns the data as a {@link PriceHistoryResponseDTO}.
     * If no data matches the criteria, returns a 404 Not Found response.
     *
     * @param productName the name of the product (required)
     * @param store optional filter by store name
     * @param brand optional filter by brand name
     * @param category optional filter by product category
     * @return a {@link ResponseEntity} containing the {@link PriceHistoryResponseDTO} if found,
     *         or 404 Not Found if no matching data exists
     */
    @GetMapping
    public ResponseEntity<PriceHistoryResponseDTO> getPriceHistory(
            @RequestParam String productName,
            @RequestParam Optional<String> store,
            @RequestParam Optional<String> brand,
            @RequestParam Optional<String> category
    ) {
        PriceHistoryResponseDTO dto = priceHistoryService.getPriceHistory(productName, store, brand, category);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}

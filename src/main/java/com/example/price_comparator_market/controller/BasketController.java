package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.BasketRequestDTO;
import com.example.price_comparator_market.dto.BasketResponseDTO;
import com.example.price_comparator_market.service.BasketOptimizerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketOptimizerService basketOptimizerService;

    /**
     * Optimizes the user's shopping basket by selecting the lowest available prices
     * for the requested products across all stores, considering any active discounts.
     * <p>
     * Returns a response containing the optimal store-to-product price mappings
     * and a list of products that could not be found.
     *
     * @param request the {@link BasketRequestDTO} containing a list of product names to search for
     * @return a {@link ResponseEntity} containing the {@link BasketResponseDTO} with the optimized basket
     */
    @PostMapping("/optimize")
    public ResponseEntity<BasketResponseDTO> optimizeBasket(@RequestBody BasketRequestDTO request) {
        return ResponseEntity.ok(basketOptimizerService.optimizeBasket(request));
    }
}
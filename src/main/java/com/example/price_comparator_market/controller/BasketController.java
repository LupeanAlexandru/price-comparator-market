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

    @PostMapping("/optimize")
    public ResponseEntity<BasketResponseDTO> optimizeBasket(@RequestBody BasketRequestDTO request) {
        return ResponseEntity.ok(basketOptimizerService.optimizeBasket(request));
    }
}
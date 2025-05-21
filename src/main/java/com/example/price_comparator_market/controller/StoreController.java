package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.StoreDTO;
import com.example.price_comparator_market.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    /**
     * Handles HTTP GET requests to retrieve all stores.
     * <p>
     * Delegates to the {@code storeService} to fetch all store data and returns it
     * as a list of {@code StoreDTO}s wrapped in a {@code ResponseEntity} with HTTP 200 OK.
     *
     * @return a {@code ResponseEntity} containing a list of {@code StoreDTO}s
     */
    @GetMapping
    public ResponseEntity<List<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeService.getAllStores());
    }

    /**
     * Handles HTTP GET requests to retrieve a store by its ID.
     * <p>
     * Delegates to the {@code storeService} to fetch the store with the given ID
     * and returns it as a {@code StoreDTO} wrapped in a {@code ResponseEntity} with HTTP 200 OK.
     *
     * @param id the ID of the store to retrieve
     * @return a {@code ResponseEntity} containing the {@code StoreDTO}
     * * @throws StoreNotFoundException if no store is found with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storeService.getStoreById(id));
    }
}
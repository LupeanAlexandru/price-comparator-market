package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.ProductDTO;
import com.example.price_comparator_market.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Handles HTTP GET requests to retrieve all products.
     * <p>
     * Delegates to the {@code productService} to fetch all products and returns the result
     * wrapped in a {@code ResponseEntity} with HTTP 200 OK status.
     *
     * @return a {@code ResponseEntity} containing a list of {@code ProductDTO}s
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    /**
     * Handles HTTP GET requests to retrieve a product by its ID.
     * <p>
     * Delegates to the {@code productService} to fetch the product with the specified ID
     * and returns it wrapped in a {@code ResponseEntity} with HTTP 200 OK status.
     *
     * @param id the ID of the product to retrieve
     * @return a {@code ResponseEntity} containing the {@code ProductDTO}
     * * @throws ProductNotFoundException if no product is found with the given ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }
}
package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.ProductSubstitutesResponseDTO;
import com.example.price_comparator_market.service.ProductSubstituteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductSubstituteController {

    private final ProductSubstituteService productSubstituteService;

    /**
     * Retrieves substitute products for a given product name.
     *
     * <p>This endpoint attempts to find alternative products based on the provided product name.
     * If no substitutes are found, a 404 (Not Found) response is returned.</p>
     *
     * @param productName the name of the product for which substitutes are being searched
     * @return a {@link ResponseEntity} containing a {@link ProductSubstitutesResponseDTO} with the list of substitute products,
     *         or a 404 response if no substitutes are found
     */
    @GetMapping("/substitutes")
    public ResponseEntity<ProductSubstitutesResponseDTO> getSubstitutes(@RequestParam String productName) {
        ProductSubstitutesResponseDTO dto = productSubstituteService.getProductsByProductName(productName);
        if (dto == null || dto.getProducts().isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }
}
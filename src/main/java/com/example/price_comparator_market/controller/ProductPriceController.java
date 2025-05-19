package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.ProductPriceDTO;
import com.example.price_comparator_market.service.ProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prices")
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    @Autowired
    public ProductPriceController(ProductPriceService productPriceService) {
        this.productPriceService = productPriceService;
    }

    @GetMapping
    public ResponseEntity<List<ProductPriceDTO>> getAllProductPrices() {
        return ResponseEntity.ok(productPriceService.getAllProductPrices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductPriceDTO> getProductPriceById(@PathVariable Long id) {
        return ResponseEntity.ok(productPriceService.getProductPriceById(id));
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<List<ProductPriceDTO>> getProductPricesByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(productPriceService.findPricesByDate(date));
    }

    @GetMapping("/store/{storeName}/date/{date}")
    public ResponseEntity<List<ProductPriceDTO>> getProductPricesByStoreAndDate(
            @PathVariable String storeName,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(productPriceService.findPricesByStoreAndDate(storeName, date));
    }

    @PostMapping("/import")
    public ResponseEntity<String> importPriceData(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("price-data-", ".csv");
            file.transferTo(tempFile.toFile());

            productPriceService.importPriceDataFromCsv(tempFile);

            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok("Price data imported successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error importing price data: " + e.getMessage());
        }
    }
}

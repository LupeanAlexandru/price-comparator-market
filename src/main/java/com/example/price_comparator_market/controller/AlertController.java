package com.example.price_comparator_market.controller;

import com.example.price_comparator_market.dto.AlertRequestDTO;
import com.example.price_comparator_market.model.Alert;
import com.example.price_comparator_market.model.Status;
import com.example.price_comparator_market.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertRepository alertRepository;

    /**
     * Creates a new price alert for a product.
     *
     * <p>This endpoint accepts a {@link AlertRequestDTO} containing the product name and target price.
     * It creates a new {@link Alert} entity with an initial {@link Status} of {@code ACTIVE}, saves it
     * to the database, and returns the saved alert.</p>
     *
     * @param dto the alert request containing product name and target price
     * @return a {@link ResponseEntity} containing the created {@link Alert}
     */
    @PostMapping
    public ResponseEntity<Alert> createAlert(@RequestBody AlertRequestDTO dto) {
        Alert alert = new Alert();
        alert.setProductName(dto.getProductName());
        alert.setTargetPrice(dto.getTargetPrice());
        alert.setStatus(Status.ACTIVE);
        alertRepository.save(alert);
        return ResponseEntity.ok(alert);
    }
}
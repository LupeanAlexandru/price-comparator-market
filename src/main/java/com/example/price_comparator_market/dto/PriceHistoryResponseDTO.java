package com.example.price_comparator_market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PriceHistoryResponseDTO {
    private String productId;
    private String productName;
    private String brand;
    private String category;
    private BigDecimal packageQuantity;
    private String packageUnit;
    private List<PriceHistoryDTO> intervals;
}
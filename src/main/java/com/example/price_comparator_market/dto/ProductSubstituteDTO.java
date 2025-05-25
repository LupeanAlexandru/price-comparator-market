package com.example.price_comparator_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductSubstituteDTO {
    private String productId;
    private String productName;
    private String brand;
    private String store;
    private BigDecimal packageQuantity;
    private String packageUnit;
    private BigDecimal price;
    private BigDecimal pricePerUnit;
    private BigDecimal discountPercentage;
    private BigDecimal finalPrice;
    private BigDecimal finalPricePerUnit;
    private boolean isBestValue;
}
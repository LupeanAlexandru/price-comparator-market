package com.example.price_comparator_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductInStoreDTO {
    private String productName;
    private BigDecimal price;
}
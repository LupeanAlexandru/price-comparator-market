package com.example.price_comparator_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class NewDiscountDTO {
    private String productName;
    private String brand;
    private BigDecimal price;
    private BigDecimal percentageOfDiscount;
    private String storeName;
}
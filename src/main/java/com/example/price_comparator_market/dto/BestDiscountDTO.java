package com.example.price_comparator_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BestDiscountDTO {
    private String productName;
    private String brand;
    private BigDecimal originalPrice;
    private BigDecimal percentageOfDiscount;
    private BigDecimal discountedPrice;
    private String storeName;
}

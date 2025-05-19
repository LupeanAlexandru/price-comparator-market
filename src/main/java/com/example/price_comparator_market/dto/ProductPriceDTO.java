package com.example.price_comparator_market.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProductPriceDTO {
    private Long id;
    private Long productId;
    private String productName;
    private Long storeId;
    private String storeName;
    private BigDecimal price;
    private BigDecimal grammage;
    private String unit;
    private LocalDate priceDate;
    private BigDecimal pricePerUnit;
}

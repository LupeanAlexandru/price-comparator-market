package com.example.price_comparator_market.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CsvPriceData {
    private String productName;
    private String category;
    private String brand;
    private BigDecimal price;
    private BigDecimal grammage;
    private String unit;
}

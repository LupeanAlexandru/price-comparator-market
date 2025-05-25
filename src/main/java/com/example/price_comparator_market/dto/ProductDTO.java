package com.example.price_comparator_market.dto;

import com.example.price_comparator_market.model.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String productId;
    private String productName;
    private String productCategory;
    private String brand;
    private BigDecimal packageQuantity;
    private String packageUnit;
    private BigDecimal price;
    private Currency currency;
}

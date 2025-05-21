package com.example.price_comparator_market.dto;

import com.example.price_comparator_market.model.Currency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDTO {
    private String product_id;
    private String product_name;
    private String product_category;
    private String brand;
    private BigDecimal package_quantity;
    private String package_unit;
    private BigDecimal price;
    private Currency currency;
}

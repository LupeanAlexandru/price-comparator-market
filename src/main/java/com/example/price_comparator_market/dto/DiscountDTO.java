package com.example.price_comparator_market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountDTO {
    private String product_id;
    private String product_name;
    private String brand;
    private BigDecimal package_quantity;
    private String package_unit;
    private String product_category;
    private LocalDate from_date;
    private LocalDate to_date;
    private BigDecimal percentage_of_discount;
}
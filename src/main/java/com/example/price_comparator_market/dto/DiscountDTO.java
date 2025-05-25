package com.example.price_comparator_market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountDTO {
    private String productId;
    private String productName;
    private String brand;
    private BigDecimal packageQuantity;
    private String packageUnit;
    private String productCategory;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal percentageOfDiscount;
}
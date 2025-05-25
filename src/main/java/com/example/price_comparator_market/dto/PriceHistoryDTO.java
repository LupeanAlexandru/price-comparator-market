package com.example.price_comparator_market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PriceHistoryDTO {
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal basePrice;
    private BigDecimal finalPrice;
    private BigDecimal discountPercentage;
    private String store;
}

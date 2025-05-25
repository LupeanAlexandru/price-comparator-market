package com.example.price_comparator_market.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class AlertRequestDTO {
    private String productName;
    private BigDecimal targetPrice;
}
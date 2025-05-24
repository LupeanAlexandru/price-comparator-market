package com.example.price_comparator_market.dto;

import lombok.Data;
import java.util.List;

@Data
public class BasketRequestDTO {
    private List<String> products;
}
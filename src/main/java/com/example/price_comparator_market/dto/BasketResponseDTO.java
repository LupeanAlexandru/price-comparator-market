package com.example.price_comparator_market.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class BasketResponseDTO {
    private Map<String, List<ProductInStoreDTO>> storeBaskets;
    private List<String> notFound;
}
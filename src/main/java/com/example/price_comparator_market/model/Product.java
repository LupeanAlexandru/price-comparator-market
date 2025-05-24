package com.example.price_comparator_market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private String productName;

    private String productCategory;

    private String brand;

    private BigDecimal packageQuantity;

    private String packageUnit;

    private BigDecimal price;

    private Currency currency;

    @ManyToOne(optional = false)
    private Store store;
}

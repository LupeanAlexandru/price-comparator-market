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
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private String productName;

    private String product_category;

    private String brand;

    private BigDecimal package_quantity;

    private String package_unit;

    private BigDecimal price;

    private Currency currency;

    @ManyToOne(optional = false)
    private Store store;
}

package com.example.price_comparator_market.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productId;

    private String productName;

    private String brand;

    private BigDecimal packageQuantity;

    private String packageUnit;

    private String productCategory;

    private LocalDate fromDate;

    private LocalDate toDate;

    private BigDecimal percentageOfDiscount;

    @ManyToOne(optional = false)
    private Store store;
}
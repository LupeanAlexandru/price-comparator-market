package com.example.price_comparator_market.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_prices")
public class ProductPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private BigDecimal grammage;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false)
    private LocalDate priceDate;

    /**
     * Calculates the price per unit based on the grammage.
     * <p>
     * If the grammage is greater than zero, this method returns the result of
     * dividing the total price by the grammage, rounded to 4 decimal places
     * using {@code RoundingMode.HALF_UP}. Otherwise, it returns the full price.
     *
     * @return the unit price as a {@code BigDecimal}
     */
    public BigDecimal getPricePerUnit() {
        if (grammage.compareTo(BigDecimal.ZERO) > 0) {
            return price.divide(grammage, 4, RoundingMode.HALF_UP);
        }
        return price;
    }
}
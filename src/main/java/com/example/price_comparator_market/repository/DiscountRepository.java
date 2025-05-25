package com.example.price_comparator_market.repository;

import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
    Optional<Discount> findByProductIdAndStoreAndFromDateAndToDate(
            String product_id, Store store, java.time.LocalDate from_date, java.time.LocalDate to_date
    );

    List<Discount> findByProductNameAndFromDateLessThanEqualAndToDateGreaterThanEqual(
            String product_name, java.time.LocalDate from, java.time.LocalDate to
    );

    List<Discount> findByFromDateLessThanEqualAndToDateGreaterThanEqual(LocalDate from, LocalDate to);

    List<Discount> findByFromDateGreaterThanEqual(LocalDate fromDate);

    List<Discount> findByProductIdAndStore(String productId, Store store);
}
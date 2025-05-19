package com.example.price_comparator_market.repository;

import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.ProductPrice;
import com.example.price_comparator_market.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {
    List<ProductPrice> findByProductAndStorePriceDate(Product product, Store store, LocalDate priceDate);
    List<ProductPrice> findByStoreAndPriceDate(Store store, LocalDate priceDate);
    List<ProductPrice> findByPriceDate(LocalDate priceDate);
}
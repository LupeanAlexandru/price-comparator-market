package com.example.price_comparator_market.repository;

import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
    Optional<Product> findByProductIdAndStore(String product_id, Store store);

    List<Product> findByProductName(String product_name);
}


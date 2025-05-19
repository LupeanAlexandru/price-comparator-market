package com.example.price_comparator_market.repository;

import com.example.price_comparator_market.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNameAndCategoryAndBrand(String name, String category, String brand);
}


package com.example.price_comparator_market.config;

import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.ProductPrice;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.ProductPriceRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import com.example.price_comparator_market.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;

    @Autowired
    public DataInitializer(
            StoreRepository storeRepository,
            ProductRepository productRepository,
            ProductPriceRepository productPriceRepository) {
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.productPriceRepository = productPriceRepository;
    }

    @Override
    public void run(String... args) {
        // Create stores
        Store lidl = new Store(null, "Lidl");
        Store kaufland = new Store(null, "Kaufland");
        Store profi = new Store(null, "Profi");

        storeRepository.saveAll(Arrays.asList(lidl, kaufland, profi));

        // Create products
        Product milk = new Product(null, "Full Fat Milk", "Dairy", "Napolact");
        Product bread = new Product(null, "White Bread", "Bakery", "Vel Pitar");
        Product eggs = new Product(null, "Fresh Eggs", "Dairy", "Farm Fresh");

        productRepository.saveAll(Arrays.asList(milk, bread, eggs));

        // Create product prices
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        List<ProductPrice> prices = Arrays.asList(
                // Today's prices
                new ProductPrice(null, milk, lidl, new BigDecimal("5.99"), new BigDecimal("1"), "liter", today),
                new ProductPrice(null, milk, kaufland, new BigDecimal("5.89"), new BigDecimal("1"), "liter", today),
                new ProductPrice(null, milk, profi, new BigDecimal("6.10"), new BigDecimal("1"), "liter", today),

                new ProductPrice(null, bread, lidl, new BigDecimal("4.50"), new BigDecimal("500"), "gram", today),
                new ProductPrice(null, bread, kaufland, new BigDecimal("4.29"), new BigDecimal("500"), "gram", today),
                new ProductPrice(null, bread, profi, new BigDecimal("4.75"), new BigDecimal("500"), "gram", today),

                new ProductPrice(null, eggs, lidl, new BigDecimal("12.99"), new BigDecimal("10"), "bucata", today),
                new ProductPrice(null, eggs, kaufland, new BigDecimal("13.50"), new BigDecimal("10"), "bucata", today),
                new ProductPrice(null, eggs, profi, new BigDecimal("12.75"), new BigDecimal("10"), "bucata", today),

                // Yesterday's prices
                new ProductPrice(null, milk, lidl, new BigDecimal("6.15"), new BigDecimal("1"), "liter", yesterday),
                new ProductPrice(null, milk, kaufland, new BigDecimal("5.89"), new BigDecimal("1"), "liter", yesterday),
                new ProductPrice(null, milk, profi, new BigDecimal("6.25"), new BigDecimal("1"), "liter", yesterday),

                new ProductPrice(null, bread, lidl, new BigDecimal("4.50"), new BigDecimal("500"), "gram", yesterday),
                new ProductPrice(null, bread, kaufland, new BigDecimal("4.45"), new BigDecimal("500"), "gram", yesterday),
                new ProductPrice(null, bread, profi, new BigDecimal("4.75"), new BigDecimal("500"), "gram", yesterday),

                new ProductPrice(null, eggs, lidl, new BigDecimal("13.25"), new BigDecimal("10"), "bucata", yesterday),
                new ProductPrice(null, eggs, kaufland, new BigDecimal("13.50"), new BigDecimal("10"), "bucata", yesterday),
                new ProductPrice(null, eggs, profi, new BigDecimal("13.00"), new BigDecimal("10"), "bucata", yesterday)
        );

        productPriceRepository.saveAll(prices);
    }
}

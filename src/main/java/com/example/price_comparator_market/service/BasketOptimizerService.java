package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.BasketRequestDTO;
import com.example.price_comparator_market.dto.BasketResponseDTO;
import com.example.price_comparator_market.dto.ProductInStoreDTO;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.DiscountRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasketOptimizerService {
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    /**
     * Optimizes a shopping basket by finding the lowest available price for each requested product,
     * taking into account both regular prices and currently active discounts across all stores.
     *
     * <p>For each product name in the {@link BasketRequestDTO}, the method determines the store offering
     * the best price and groups the results by store. If a product is not found in any store, it is included
     * in a separate list of missing items.</p>
     *
     * @param request the basket request containing a list of product names
     * @return a {@link BasketResponseDTO} containing a mapping of store names to the list of
     *         found products with their lowest prices, and a list of product names that could not be found
     */
    public BasketResponseDTO optimizeBasket(BasketRequestDTO request) {
        Map<String, List<ProductInStoreDTO>> storeBaskets = new HashMap<>();
        List<String> notFound = new ArrayList<>();
        LocalDate today = LocalDate.now();

        for (String productName : request.getProducts()) {
            List<Discount> discounts = discountRepository.findByProductNameAndFromDateLessThanEqualAndToDateGreaterThanEqual(
                    productName, today, today
            );

            Map<Store, BigDecimal> storeToDiscountedPrice = new HashMap<>();
            for (Discount discount : discounts) {
                Optional<Product> productOpt = productRepository.findByProductIdAndStore(discount.getProductId(), discount.getStore());
                if (productOpt.isPresent()) {
                    BigDecimal price = productOpt.get().getPrice();
                    BigDecimal discountedPrice = price.subtract(price.multiply(discount.getPercentageOfDiscount()).divide(BigDecimal.valueOf(100)));
                    storeToDiscountedPrice.put(discount.getStore(), discountedPrice);
                }
            }

            List<Product> products = productRepository.findByProductName(productName);
            for (Product product : products) {
                Store store = product.getStore();
                BigDecimal price = product.getPrice();
                if (!storeToDiscountedPrice.containsKey(store) || price.compareTo(storeToDiscountedPrice.get(store)) < 0) {
                    storeToDiscountedPrice.put(store, price);
                }
            }

            Store bestStore = null;
            BigDecimal bestPrice = null;
            for (Map.Entry<Store, BigDecimal> entry : storeToDiscountedPrice.entrySet()) {
                if (bestPrice == null || entry.getValue().compareTo(bestPrice) < 0) {
                    bestStore = entry.getKey();
                    bestPrice = entry.getValue();
                }
            }
            if (bestStore != null) {
                storeBaskets.computeIfAbsent(bestStore.getName(), k -> new ArrayList<>())
                        .add(new ProductInStoreDTO(productName, bestPrice));
            } else {
                notFound.add(productName);
            }
        }

        BasketResponseDTO response = new BasketResponseDTO();
        response.setStoreBaskets(storeBaskets);
        response.setNotFound(notFound);
        return response;
    }
}

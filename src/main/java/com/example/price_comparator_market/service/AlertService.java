package com.example.price_comparator_market.service;

import com.example.price_comparator_market.model.Alert;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.Status;
import com.example.price_comparator_market.repository.AlertRepository;
import com.example.price_comparator_market.repository.DiscountRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertService {

    private final AlertRepository alertRepository;
    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    /**
     * Scheduled task that processes active price alerts.
     *
     * <p>This method runs at a fixed interval (24 hours - reason behind this is that we should
     * only check once a day for discounts, as stores will most likely not
     * change prices or add discounts with immediate effect). It fetches all alerts
     * with {@link Status#ACTIVE}, checks all matching products by name, and calculates the
     * final price including any active discounts.</p>
     *
     * <p>If the final price of any matching product is less than or equal to the target price
     * specified in the alert, the alert is marked as {@link Status#PROCESSED} and saved.
     * A log message is also printed indicating that the alert was triggered.</p>
     *
     * <p>This method is intended to be used as a background scheduler for notifying
     * users when their desired price point is met.</p>
     */
    // use just 60 * 1000 for testing purposes
    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void processAlerts() {
        List<Alert> activeAlerts = alertRepository.findByStatus(Status.ACTIVE);
        LocalDate today = LocalDate.now();

        for (Alert alert : activeAlerts) {
            List<Product> products = productRepository.findAll().stream()
                    .filter(p -> p.getProductName().equalsIgnoreCase(alert.getProductName()))
                    .toList();

            for (Product product : products) {
                List<Discount> discounts = discountRepository.findByProductIdAndStore(product.getProductId(), product.getStore());
                Optional<Discount> activeDiscount = discounts.stream()
                        .filter(d -> !today.isBefore(d.getFromDate()) && !today.isAfter(d.getToDate()))
                        .max(Comparator.comparing(Discount::getPercentageOfDiscount));

                BigDecimal finalPrice = product.getPrice();
                if (activeDiscount.isPresent()) {
                    BigDecimal discount = activeDiscount.get().getPercentageOfDiscount();
                    finalPrice = finalPrice.subtract(finalPrice.multiply(discount).divide(BigDecimal.valueOf(100)));
                }

                if (finalPrice.compareTo(alert.getTargetPrice()) <= 0) {
                    alert.setStatus(Status.PROCESSED);
                    alert.setProcessedAt(LocalDateTime.now());
                    alertRepository.save(alert);
                    log.info("Alert triggered for product: {} at price {}", product.getProductName(), finalPrice);
                    break;
                }
            }
        }
    }
}
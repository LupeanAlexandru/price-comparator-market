package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.PriceHistoryDTO;
import com.example.price_comparator_market.dto.PriceHistoryResponseDTO;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.repository.DiscountRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PriceHistoryService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    public PriceHistoryResponseDTO getPriceHistory(
            String productName,
            Optional<String> storeName,
            Optional<String> brand,
            Optional<String> category
    ) {

        List<Product> products = productRepository.findByProductName(productName).stream()
                .filter(p -> storeName.map(s -> p.getStore().getName().equalsIgnoreCase(s)).orElse(true))
                .filter(p -> brand.map(b -> p.getBrand().equalsIgnoreCase(b)).orElse(true))
                .filter(p -> category.map(c -> p.getProductCategory().equalsIgnoreCase(c)).orElse(true))
                .collect(Collectors.toList());

        if (products.isEmpty()) return null;

        Product mainProduct = products.getFirst();

        List<PriceHistoryDTO> intervals = new ArrayList<>();

        for (Product product : products) {
            List<Discount> discounts = discountRepository.findByProductIdAndStore(product.getProductId(), product.getStore());

            List<Interval> allIntervals = new ArrayList<>();
            for (Discount discount : discounts) {
                allIntervals.add(new Interval(
                        discount.getFromDate(),
                        discount.getToDate(),
                        discount.getPercentageOfDiscount()
                ));
            }

            List<Interval> merged = mergeIntervals(allIntervals);

            LocalDate minDiscountDate = merged.stream().map(i -> i.fromDate).min(LocalDate::compareTo).orElse(null);
            LocalDate maxDiscountDate = merged.stream().map(i -> i.toDate).max(LocalDate::compareTo).orElse(null);

            LocalDate timelineStart = minDiscountDate != null ? minDiscountDate.minusDays(30) : LocalDate.now().minusDays(30);
            LocalDate timelineEnd = maxDiscountDate != null ? maxDiscountDate : LocalDate.now();

            List<Interval> fullIntervals = new ArrayList<>();

            if (minDiscountDate != null && timelineStart.isBefore(minDiscountDate)) {
                fullIntervals.add(new Interval(timelineStart, minDiscountDate.minusDays(1), BigDecimal.ZERO));
            }

            for (int i = 0; i < merged.size(); i++) {
                Interval current = merged.get(i);
                fullIntervals.add(current);

                if (i < merged.size() - 1) {
                    Interval next = merged.get(i + 1);
                    if (current.toDate.plusDays(1).isBefore(next.fromDate)) {
                        fullIntervals.add(new Interval(
                                current.toDate.plusDays(1),
                                next.fromDate.minusDays(1),
                                BigDecimal.ZERO
                        ));
                    }
                }
            }

            if (maxDiscountDate != null && maxDiscountDate.isBefore(timelineEnd)) {
                fullIntervals.add(new Interval(maxDiscountDate.plusDays(1), timelineEnd, BigDecimal.ZERO));
            }

            for (Interval interval : fullIntervals) {
                BigDecimal basePrice = product.getPrice();
                BigDecimal discount = interval.discountPercentage;
                BigDecimal finalPrice = basePrice.subtract(basePrice.multiply(discount).divide(BigDecimal.valueOf(100)));
                intervals.add(new PriceHistoryDTO(
                        interval.fromDate,
                        interval.toDate,
                        basePrice,
                        finalPrice,
                        discount,
                        product.getStore().getName()
                ));
            }
        }

        intervals.sort(Comparator.comparing(PriceHistoryDTO::getFromDate));

        PriceHistoryResponseDTO dto = new PriceHistoryResponseDTO();
        dto.setProductId(mainProduct.getProductId());
        dto.setProductName(mainProduct.getProductName());
        dto.setBrand(mainProduct.getBrand());
        dto.setCategory(mainProduct.getProductCategory());
        dto.setPackageQuantity(mainProduct.getPackageQuantity());
        dto.setPackageUnit(mainProduct.getPackageUnit());
        dto.setIntervals(intervals);

        return dto;
    }

    private static class Interval {
        LocalDate fromDate;
        LocalDate toDate;
        BigDecimal discountPercentage;

        Interval(LocalDate from, LocalDate to, BigDecimal discount) {
            this.fromDate = from;
            this.toDate = to;
            this.discountPercentage = discount;
        }
    }

    private List<Interval> mergeIntervals(List<Interval> intervals) {
        if (intervals.isEmpty()) return Collections.emptyList();

        Set<LocalDate> datePoints = new HashSet<>();
        for (Interval i : intervals) {
            LocalDate d = i.fromDate;
            while (!d.isAfter(i.toDate)) {
                datePoints.add(d);
                d = d.plusDays(1);
            }
        }
        List<LocalDate> sortedDates = new ArrayList<>(datePoints);
        Collections.sort(sortedDates);

        List<Interval> merged = new ArrayList<>();
        if (sortedDates.isEmpty()) return merged;

        LocalDate currentStart = sortedDates.getFirst();
        LocalDate currentEnd = currentStart;
        BigDecimal currentDiscount = getMaxDiscountForDay(currentStart, intervals);

        for (int i = 1; i < sortedDates.size(); i++) {
            LocalDate day = sortedDates.get(i);
            BigDecimal discount = getMaxDiscountForDay(day, intervals);
            if (discount.equals(currentDiscount) && day.equals(currentEnd.plusDays(1))) {
                currentEnd = day;
            } else {
                merged.add(new Interval(currentStart, currentEnd, currentDiscount));
                currentStart = day;
                currentEnd = day;
                currentDiscount = discount;
            }
        }
        merged.add(new Interval(currentStart, currentEnd, currentDiscount));
        return merged;
    }

    private BigDecimal getMaxDiscountForDay(LocalDate day, List<Interval> intervals) {
        return intervals.stream()
                .filter(i -> (!day.isBefore(i.fromDate) && !day.isAfter(i.toDate)))
                .map(i -> i.discountPercentage)
                .max(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
    }
}
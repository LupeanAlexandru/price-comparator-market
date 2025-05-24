package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.BestDiscountDTO;
import com.example.price_comparator_market.dto.DiscountDTO;
import com.example.price_comparator_market.dto.NewDiscountDTO;
import com.example.price_comparator_market.exception.DiscountNotFoundException;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.repository.DiscountRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

    private final ProductRepository productRepository;

    /**
     * Retrieves all discounts from the repository.
     *
     * @return a list of {@link DiscountDTO} objects representing all discounts
     */
    public List<DiscountDTO> getAllDiscounts() {
        return discountRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a discount by its unique identifier.
     *
     * @param id the ID of the discount to retrieve
     * @return a {@link DiscountDTO} representing the discount with the specified ID
     * @throws DiscountNotFoundException if no discount is found with the given ID
     */
    public DiscountDTO getDiscountById(Long id) {
        return discountRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new DiscountNotFoundException("Discount not found with id: " + id));
    }

    /**
     * Retrieves a list of the best discounts available for all products on the specified date.
     * <p>
     * For each valid discount entry active on the given date, the method calculates the discounted price
     * using the original price of the associated product and the discount percentage.
     * Only discounts for which a corresponding product with a known original price exists are included.
     * The resulting list is sorted in descending order by the percentage of discount.
     *
     * @param date the date for which to retrieve applicable discounts
     * @return a sorted list of {@link BestDiscountDTO} objects containing product name, brand,
     *         original price, discount percentage, discounted price, and store name
     */
    public List<BestDiscountDTO> getBestDiscounts(LocalDate date) {
        List<Discount> discounts = discountRepository.findByFromDateLessThanEqualAndToDateGreaterThanEqual(date, date);

        return discounts.stream().map(discount -> {
            Optional<Product> product = productRepository.findByProductIdAndStore(discount.getProductId(), discount.getStore());
            BigDecimal originalPrice = product.map(Product::getPrice).orElse(null);
            BigDecimal discountedPrice = null;
            if (originalPrice != null) {
                discountedPrice = originalPrice.subtract(
                        originalPrice.multiply(discount.getPercentageOfDiscount()).divide(BigDecimal.valueOf(100))
                );
            }
            return new BestDiscountDTO(
                    discount.getProductName(),
                    discount.getBrand(),
                    originalPrice,
                    discount.getPercentageOfDiscount(),
                    discountedPrice,
                    discount.getStore().getName()
            );
        }).filter(dto -> dto.getOriginalPrice() != null && dto.getDiscountedPrice() != null).sorted(Comparator.comparing(BestDiscountDTO::getPercentageOfDiscount).reversed()).collect(Collectors.toList());
    }

    /**
     * Retrieves a list of newly added discounts from the past day.
     * <p>
     * A discount is considered "new" if its {@code fromDate} is greater than or equal to yesterday
     * (added within the last 24 hours). Each returned discount includes product name, brand,
     * price, discount percentage, and store name.
     * <p>
     *
     * @return a list of {@link NewDiscountDTO} objects representing newly added discounts
     */
    public List<NewDiscountDTO> getNewDiscounts() {
        LocalDate since = LocalDate.now().minusDays(1);
        List<Discount> newDiscounts = discountRepository.findByFromDateGreaterThanEqual(since);

        return newDiscounts.stream()
                .map(discount -> {
                    Optional<Product> productOpt = productRepository.findByProductIdAndStore(discount.getProductId(), discount.getStore());
                    BigDecimal originalPrice = productOpt.map(Product::getPrice).orElse(null);
                    return new NewDiscountDTO(
                            discount.getProductName(),
                            discount.getBrand(),
                            originalPrice,
                            discount.getPercentageOfDiscount(),
                            discount.getStore().getName()
                    );
                })
                .filter(dto -> dto.getPrice() != null)
                .collect(Collectors.toList());
    }

    private DiscountDTO mapToDTO(Discount discount) {
        DiscountDTO dto = new DiscountDTO();
        dto.setProduct_id(discount.getProductId());
        dto.setProduct_name(discount.getProductName());
        dto.setBrand(discount.getBrand());
        dto.setPackage_quantity(discount.getPackageQuantity());
        dto.setPackage_unit(discount.getPackageUnit());
        dto.setProduct_category(discount.getProductCategory());
        dto.setFrom_date(discount.getFromDate());
        dto.setTo_date(discount.getToDate());
        dto.setPercentage_of_discount(discount.getPercentageOfDiscount());
        return dto;
    }
}
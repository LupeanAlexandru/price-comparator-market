package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.ProductSubstituteDTO;
import com.example.price_comparator_market.dto.ProductSubstitutesResponseDTO;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.repository.DiscountRepository;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSubstituteService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;

    /**
     * Finds all product variants (substitutes) with the given product name and calculates pricing details,
     * including any active discounts and price per unit.
     *
     * <p>For each matching product, this method determines if a discount is currently active (based on the current date),
     * calculates the final price after the discount (if applicable), and computes the price per unit both before and
     * after the discount. It then marks the best value substitute.</p>
     *
     * @param productName the name of the product for which substitutes are being retrieved
     * @return a {@link ProductSubstitutesResponseDTO} containing a list of {@link ProductSubstituteDTO},
     *         sorted by final price per unit in ascending order, with the best value(s) flagged
     */
    public ProductSubstitutesResponseDTO getProductsByProductName(String productName) {
        LocalDate today = LocalDate.now();

        List<Product> products = productRepository.findAll().stream()
                .filter(p -> p.getProductName().equalsIgnoreCase(productName))
                .collect(Collectors.toList());

        List<ProductSubstituteDTO> dtos = products.stream()
                .map(p -> {
                    BigDecimal pricePerUnit = p.getPrice().divide(p.getPackageQuantity(), 2, RoundingMode.HALF_UP);

                    List<Discount> discounts = discountRepository.findByProductIdAndStore(p.getProductId(), p.getStore());
                    Discount activeDiscount = discounts.stream()
                            .filter(d -> !today.isBefore(d.getFromDate()) && !today.isAfter(d.getToDate()))
                            .max(Comparator.comparing(Discount::getPercentageOfDiscount))
                            .orElse(null);

                    BigDecimal discountPercentage = activeDiscount != null ? activeDiscount.getPercentageOfDiscount() : BigDecimal.ZERO;
                    BigDecimal finalPrice = p.getPrice();
                    if (activeDiscount != null) {
                        finalPrice = finalPrice.subtract(finalPrice.multiply(discountPercentage).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                    }
                    BigDecimal finalPricePerUnit = finalPrice.divide(p.getPackageQuantity(), 2, RoundingMode.HALF_UP);

                    return new ProductSubstituteDTO(
                            p.getProductId(),
                            p.getProductName(),
                            p.getBrand(),
                            p.getStore().getName(),
                            p.getPackageQuantity(),
                            p.getPackageUnit(),
                            p.getPrice().setScale(2, RoundingMode.HALF_UP),
                            pricePerUnit,
                            discountPercentage,
                            finalPrice.setScale(2, RoundingMode.HALF_UP),
                            finalPricePerUnit,
                            false
                    );
                })
                .sorted(Comparator.comparing(ProductSubstituteDTO::getFinalPricePerUnit))
                .collect(Collectors.toList());

        if (!dtos.isEmpty()) {
            BigDecimal bestPerUnit = dtos.getFirst().getFinalPricePerUnit();
            dtos.forEach(dto -> {
                if (dto.getFinalPricePerUnit().compareTo(bestPerUnit) == 0) {
                    dto.setBestValue(true);
                }
            });
        }

        ProductSubstitutesResponseDTO response = new ProductSubstitutesResponseDTO();
        response.setProducts(dtos);
        return response;
    }
}
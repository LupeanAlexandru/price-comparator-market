package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.DiscountDTO;
import com.example.price_comparator_market.exception.DiscountNotFoundException;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.repository.DiscountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;

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

    private DiscountDTO mapToDTO(Discount discount) {
        DiscountDTO dto = new DiscountDTO();
        dto.setProduct_id(discount.getProductId());
        dto.setProduct_name(discount.getProductName());
        dto.setBrand(discount.getBrand());
        dto.setPackage_quantity(discount.getPackageQuantity());
        dto.setPackage_unit(discount.getPackage_unit());
        dto.setProduct_category(discount.getProduct_category());
        dto.setFrom_date(discount.getFromDate());
        dto.setTo_date(discount.getToDate());
        dto.setPercentage_of_discount(discount.getPercentage_of_discount());
        return dto;
    }
}
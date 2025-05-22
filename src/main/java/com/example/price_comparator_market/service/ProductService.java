package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.ProductDTO;
import com.example.price_comparator_market.exception.ProductNotFoundException;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Retrieves all products from the repository and converts them to DTOs.
     * <p>
     * This method fetches all {@code Product} entities, maps each to a {@code ProductDTO},
     * and returns them as a list.
     *
     * @return a list of all products represented as {@code ProductDTO}s
     */
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product by its ID and converts it to a DTO.
     * <p>
     * If a product with the specified ID exists in the repository, it is returned
     * as a {@code ProductDTO}. Otherwise, a {@code RuntimeException} is thrown.
     *
     * @param id the ID of the product to retrieve
     * @return the {@code ProductDTO} corresponding to the given ID
     * @throws ProductNotFoundException if no product is found with the specified ID
     */
    public ProductDTO getProductById(String id) {
        return productRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new ProductNotFoundException(String.format("Product not found with id: %s", id)));
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProduct_id(product.getProductId());
        dto.setProduct_name(product.getProduct_name());
        dto.setProduct_category(product.getProduct_category());
        dto.setBrand(product.getBrand());
        dto.setPackage_quantity(product.getPackage_quantity());
        dto.setPackage_unit(product.getPackage_unit());
        dto.setPrice(product.getPrice());
        dto.setCurrency(product.getCurrency());
        return dto;
    }
}
package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.ProductDTO;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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
     * @throws RuntimeException if no product is found with the specified ID
     */
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    /**
     * Retrieves an existing product by its name, category, and brand,
     * or creates and saves a new one if it does not exist.
     * <p>
     * If a product with the specified name, category, and brand is found
     * in the repository, it is returned. Otherwise, a new {@code Product}
     * is created with the provided details and saved to the repository.
     *
     * @param name     the name of the product
     * @param category the category of the product
     * @param brand    the brand of the product
     * @return the existing or newly created {@code Product}
     */
    public Product getOrCreateProduct(String name, String category, String brand) {
        Optional<Product> existingProduct = productRepository.findByNameAndCategoryAndBrand(name, category, brand);
        if (existingProduct.isPresent()) {
            return existingProduct.get();
        } else {
            Product newProduct = new Product();
            newProduct.setName(name);
            newProduct.setCategory(category);
            newProduct.setBrand(brand);
            return productRepository.save(newProduct);
        }
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        return dto;
    }
}
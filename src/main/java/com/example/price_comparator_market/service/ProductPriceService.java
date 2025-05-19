package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.CsvPriceData;
import com.example.price_comparator_market.dto.ProductPriceDTO;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.ProductPrice;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.ProductPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;
    private final ProductService productService;
    private final StoreService storeService;
    private final CsvImportService csvImportService;

    // Regular expression to extract store name and date from filename
    private static final Pattern FILE_PATTERN = Pattern.compile("^(.+?)_(\\d{4}-\\d{2}-\\d{2})\\.csv$");

    @Autowired
    public ProductPriceService(
            ProductPriceRepository productPriceRepository,
            ProductService productService,
            StoreService storeService,
            CsvImportService csvImportService) {
        this.productPriceRepository = productPriceRepository;
        this.productService = productService;
        this.storeService = storeService;
        this.csvImportService = csvImportService;
    }

    /**
     * Retrieves all product prices from the repository and converts them to DTOs.
     * <p>
     * This method fetches all {@code ProductPrice} entities, maps each to a {@code ProductPriceDTO},
     * and returns them as a list.
     *
     * @return a list of all {@code ProductPriceDTO}s
     */
    public List<ProductPriceDTO> getAllProductPrices() {
        return productPriceRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a product price by its ID and converts it to a DTO.
     * <p>
     * If a product price with the given ID exists, it is returned as a {@code ProductPriceDTO}.
     * Otherwise, a {@code RuntimeException} is thrown.
     *
     * @param id the ID of the product price to retrieve
     * @return the {@code ProductPriceDTO} with the specified ID
     * @throws RuntimeException if no product price is found with the specified ID
     */
    public ProductPriceDTO getProductPriceById(Long id) {
        return productPriceRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("ProductPrice not found with id: " + id));
    }

    /**
     * Imports product price data from a CSV file and stores it in the repository.
     * <p>
     * The filename must follow the pattern {@code storename_yyyy-MM-dd.csv}. The store name
     * and pricing date are extracted from the filename. If the store does not exist,
     * it is created. The CSV contents are then parsed into product pricing data,
     * which is saved to the database.
     * <p>
     * This method is transactional to ensure all records are saved as a single unit of work.
     *
     * @param filePath the path to the CSV file to import
     * @throws RuntimeException if the filename format is invalid or if an error occurs during import
     */
    @Transactional
    public void importPriceDataFromCsv(Path filePath) {
        String filename = filePath.getFileName().toString();
        Matcher matcher = FILE_PATTERN.matcher(filename);

        if (!matcher.matches()) {
            throw new RuntimeException("Invalid filename format. Expected: storename_yyyy-MM-dd.csv");
        }

        String storeName = matcher.group(1);
        LocalDate priceDate = LocalDate.parse(matcher.group(2));

        Store store = storeService.getOrCreateStore(storeName);
        List<CsvPriceData> priceDataList = csvImportService.importPriceData(filePath);

        for (CsvPriceData csvData : priceDataList) {
            Product product = productService.getOrCreateProduct(
                    csvData.getProductName(),
                    csvData.getCategory(),
                    csvData.getBrand()
            );

            ProductPrice productPrice = new ProductPrice();
            productPrice.setProduct(product);
            productPrice.setStore(store);
            productPrice.setPrice(csvData.getPrice());
            productPrice.setGrammage(csvData.getGrammage());
            productPrice.setUnit(csvData.getUnit());
            productPrice.setPriceDate(priceDate);

            productPriceRepository.save(productPrice);
        }
    }

    /**
     * Retrieves all product prices for a given date and converts them to DTOs.
     * <p>
     * This method queries the repository for all {@code ProductPrice} entries that
     * match the provided date, maps each to a {@code ProductPriceDTO}, and returns
     * them as a list.
     *
     * @param date the date for which to retrieve product prices
     * @return a list of {@code ProductPriceDTO}s for the given date
     */
    public List<ProductPriceDTO> findPricesByDate(LocalDate date) {
        return productPriceRepository.findByPriceDate(date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all product prices for a specific store and date, and converts them to DTOs.
     * <p>
     * If the store does not exist, it will be created. Then, this method queries the repository
     * for all {@code ProductPrice} entries associated with the specified store and date,
     * maps each to a {@code ProductPriceDTO}, and returns them as a list.
     *
     * @param storeName the name of the store
     * @param date      the date for which to retrieve product prices
     * @return a list of {@code ProductPriceDTO}s for the given store and date
     */
    public List<ProductPriceDTO> findPricesByStoreAndDate(String storeName, LocalDate date) {
        Store store = storeService.getOrCreateStore(storeName);
        return productPriceRepository.findByStoreAndPriceDate(store, date).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private ProductPriceDTO mapToDTO(ProductPrice productPrice) {
        ProductPriceDTO dto = new ProductPriceDTO();
        dto.setId(productPrice.getId());
        dto.setProductId(productPrice.getProduct().getId());
        dto.setProductName(productPrice.getProduct().getName());
        dto.setStoreId(productPrice.getStore().getId());
        dto.setStoreName(productPrice.getStore().getName());
        dto.setPrice(productPrice.getPrice());
        dto.setGrammage(productPrice.getGrammage());
        dto.setUnit(productPrice.getUnit());
        dto.setPriceDate(productPrice.getPriceDate());
        dto.setPricePerUnit(productPrice.getPricePerUnit());
        return dto;
    }
}
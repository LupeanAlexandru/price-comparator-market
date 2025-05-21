package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.ProductDTO;
import com.example.price_comparator_market.exception.BusinessException;
import com.example.price_comparator_market.model.Currency;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.ProductRepository;
import com.example.price_comparator_market.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    /**
     * Imports product data from a CSV file and saves it to the repository.
     * <p>
     * The filename is expected to contain the store name as the prefix before an underscore (e.g., "StoreName_2023-05-21.csv").
     * The method attempts to find an existing {@code Store} by this name, or creates and saves a new one if not found.
     * <p>
     * The CSV file is expected to have a header row with columns:
     * "product_id", "product_name", "product_category", "brand", "package_quantity", "package_unit", "price" and "currency".
     * Each record is parsed into a {@code Product} entity linked to the store and saved to the database.
     * Corresponding {@code ProductDTO} objects are returned in a list.
     * <p>
     * Throws a {@code BusinessException} if there is an error reading the CSV file.
     *
     * @param filePath the path to the CSV file to import
     * @return a list of {@code ProductDTO} representing the imported products
     * @throws BusinessException if an IOException occurs during CSV file processing
     */
    public List<ProductDTO> importCsv(Path filePath) {
        String filename = filePath.getFileName().toString();
        String storeName = capitalize(filename.split("_")[0]);

        Store store = storeRepository.findByName(storeName)
                .orElseGet(() -> {
                    Store s = new Store();
                    s.setName(storeName);
                    return storeRepository.save(s);
                });

        List<ProductDTO> productDTOList = new ArrayList<>();

        try (Reader reader = Files.newBufferedReader(filePath)) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            for (CSVRecord csvRecord : csvParser) {
                Product product = new Product();
                product.setProduct_id(csvRecord.get("product_id"));
                product.setProduct_name(csvRecord.get("product_name"));
                product.setProduct_category(csvRecord.get("product_category"));
                product.setBrand(csvRecord.get("brand"));
                product.setPackage_quantity(new BigDecimal(csvRecord.get("package_quantity")));
                product.setPackage_unit(csvRecord.get("package_unit"));
                product.setPrice(new BigDecimal(csvRecord.get("price")));
                product.setCurrency(Currency.valueOf(csvRecord.get("currency")));
                product.setStore(store);

                productRepository.save(product);

                ProductDTO dto = new ProductDTO();
                dto.setProduct_id(product.getProduct_id());
                dto.setProduct_name(product.getProduct_name());
                dto.setProduct_category(product.getProduct_category());
                dto.setBrand(product.getBrand());
                dto.setPackage_quantity(product.getPackage_quantity());
                dto.setPackage_unit(product.getPackage_unit());
                dto.setPrice(product.getPrice());
                dto.setCurrency(product.getCurrency());

                productDTOList.add(dto);
            }
        } catch (IOException e) {
            throw new BusinessException("Error importing CSV data: " + e.getMessage());
        }

        return productDTOList;
    }

    /**
     * Capitalizes the first letter of the given string and converts the rest to lowercase.
     * <p>
     * If the input string is {@code null} or empty, it is returned unchanged.
     *
     * @param str the string to capitalize
     * @return the capitalized string with the first letter uppercase and the rest lowercase,
     *         or the original string if {@code null} or empty
     */
    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
}

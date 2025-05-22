package com.example.price_comparator_market.service;

import com.example.price_comparator_market.exception.CsvImportException;
import com.example.price_comparator_market.model.Currency;
import com.example.price_comparator_market.model.Discount;
import com.example.price_comparator_market.model.Product;
import com.example.price_comparator_market.model.Store;
import com.example.price_comparator_market.repository.DiscountRepository;
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
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CsvImportService {

    private final StoreRepository storeRepository;

    private final ProductRepository productRepository;

    private final DiscountRepository discountRepository;

    /**
     * Imports product or discount data from a CSV file and saves it to the repository.
     * <p>
     * The filename must follow a specific format where the store name appears as the prefix before an underscore
     * (e.g., {@code StoreName_2023-05-21.csv} or {@code StoreName_discounts_2023-05-21.csv}).
     * The store name is extracted from the filename, and an existing {@code Store} entity is retrieved by name.
     * If no such store exists, a new one is created and saved.
     * <p>
     * If the filename contains {@code "_discounts_"}, the file is treated as containing discount data.
     * Otherwise, it is treated as containing product data.
     * Throws a {@code CsvImportException} if there is an error while reading or parsing the file.
     *
     * @param filePath the path to the CSV file to import
     * @throws CsvImportException if an {@link IOException} occurs during CSV processing
     */
    public void importCsv(Path filePath) {
        String filename = filePath.getFileName().toString();
        String storeName = capitalize(filename.split("_")[0]);

        Store store = storeRepository.findByName(storeName)
                .orElseGet(() -> {
                    Store s = new Store();
                    s.setName(storeName);
                    return storeRepository.save(s);
                });

        try (Reader reader = Files.newBufferedReader(filePath)) {
            CSVFormat format = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser csvParser = format.parse(reader);

            if (filename.contains("_discounts_")) {
                for (CSVRecord csvRecord : csvParser) {
                    String productId = csvRecord.get("product_id");
                    LocalDate fromDate = LocalDate.parse(csvRecord.get("from_date"));
                    LocalDate toDate = LocalDate.parse(csvRecord.get("to_date"));

                    boolean exists = discountRepository.findByProductIdAndStoreAndFromDateAndToDate(
                            productId, store, fromDate, toDate
                    ).isPresent();
                    if (exists) continue;

                    Discount discount = mapCsvRecordToDiscount(csvRecord, store);

                    discountRepository.save(discount);
                }
            } else {

                for (CSVRecord csvRecord : csvParser) {
                    String productId = csvRecord.get("product_id");

                    boolean exists = productRepository.findByProductIdAndStore(productId, store).isPresent();

                    if (exists) {
                        continue;
                    }

                    Product product = mapCsvRecordToProduct(csvRecord, store);

                    productRepository.save(product);
                }
            }
        } catch (IOException e) {
            throw new CsvImportException("Error importing CSV data: " + e.getMessage());
        }

    }

    private Product mapCsvRecordToProduct(CSVRecord csvRecord, Store store) {
        Product product = new Product();
        product.setProductId(csvRecord.get("product_id"));
        product.setProduct_name(csvRecord.get("product_name"));
        product.setProduct_category(csvRecord.get("product_category"));
        product.setBrand(csvRecord.get("brand"));
        product.setPackage_quantity(new BigDecimal(csvRecord.get("package_quantity")));
        product.setPackage_unit(csvRecord.get("package_unit"));
        product.setPrice(new BigDecimal(csvRecord.get("price")));
        product.setCurrency(Currency.valueOf(csvRecord.get("currency")));
        product.setStore(store);
        return product;
    }

    private Discount mapCsvRecordToDiscount(CSVRecord csvRecord, Store store) {
        Discount discount = new Discount();
        discount.setProductId(csvRecord.get("product_id"));
        discount.setProduct_name(csvRecord.get("product_name"));
        discount.setBrand(csvRecord.get("brand"));
        discount.setPackage_quantity(new BigDecimal(csvRecord.get("package_quantity")));
        discount.setPackage_unit(csvRecord.get("package_unit"));
        discount.setProduct_category(csvRecord.get("product_category"));
        discount.setFromDate(LocalDate.parse(csvRecord.get("from_date")));
        discount.setToDate(LocalDate.parse(csvRecord.get("to_date")));
        discount.setPercentage_of_discount(new BigDecimal(csvRecord.get("percentage_of_discount")));
        discount.setStore(store);
        return discount;
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

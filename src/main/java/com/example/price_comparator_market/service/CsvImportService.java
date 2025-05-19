package com.example.price_comparator_market.service;

import com.example.price_comparator_market.dto.CsvPriceData;
import com.example.price_comparator_market.exception.BusinessException;
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
public class CsvImportService {

    public List<CsvPriceData> importPriceData(Path filePath) {
        try (Reader reader = Files.newBufferedReader(filePath)) {
            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .parse(reader);

            List<CsvPriceData> priceDataList = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                CsvPriceData priceData = new CsvPriceData();
                priceData.setProductName(csvRecord.get("product_name"));
                priceData.setCategory(csvRecord.get("category"));
                priceData.setBrand(csvRecord.get("brand"));
                priceData.setPrice(new BigDecimal(csvRecord.get("price")));
                priceData.setGrammage(new BigDecimal(csvRecord.get("grammage")));
                priceData.setUnit(csvRecord.get("unit"));

                priceDataList.add(priceData);
            }

            return priceDataList;
        } catch (IOException e) {
            throw new BusinessException("Error importing CSV data: " + e.getMessage());
        }
    }
}

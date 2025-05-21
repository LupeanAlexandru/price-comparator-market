package com.example.price_comparator_market.config;

import com.example.price_comparator_market.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CsvImportService csvImportService;

    @Override
    public void run(String... args) {
       List<String> csvFiles = List.of(
               "lidl_2025-05-08.csv",
               "kaufland_2025-05-08.csv",
               "profi_2025-05-08.csv"
       );
       for (String fileName : csvFiles) {
           Path path = Path.of("src/main/resources/csv/" + fileName);
           csvImportService.importCsv(path);
       }
    }
}

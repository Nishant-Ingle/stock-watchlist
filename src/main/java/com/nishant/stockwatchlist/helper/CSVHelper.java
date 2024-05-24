package com.nishant.stockwatchlist.helper;

import com.nishant.stockwatchlist.model.Stock;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Helper class for reading CSV data from file.
 */
@Slf4j
public class CSVHelper {
    public static List<Stock> getStockData(String filePath) {
        List<Stock> stocks = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(filePath).getFile()))) {
            // Skip the header row
            reader.readNext();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String companyName = nextLine[0].trim();
                String symbol = nextLine[1].trim();
                double price = Double.parseDouble(nextLine[2]);
                Stock stock = new Stock(UUID.randomUUID(), symbol, companyName, price);
                stocks.add(stock);
            }
        } catch (Exception exception) {
            log.error(exception.toString());
        }

        return stocks;
    }
}

package com.nishant.stockwatchlist.helper;

import com.nishant.stockwatchlist.model.Stock;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Helper class for reading CSV data from file.
 */
@Slf4j
public class CSVHelper {

    /**
     * Reads the CSV file present at filePath and return list of Stock.
     * @param filePath Path of the CSV file.
     * @return List of stocks.
     */
    public static Set<Stock> getStockData(String filePath) {
        Set<Stock> stocks = new HashSet<>();

        try (CSVReader reader = new CSVReader(new FileReader(new ClassPathResource(filePath).getFile()))) {
            // Skip the header row
            reader.readNext();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                String companyName = nextLine[0].trim();
                String symbol = nextLine[1].trim();
                double price = Double.parseDouble(nextLine[2]);
                Stock stock = Stock.of(symbol, companyName);
                stock.setPrice(price);
                stocks.add(stock);
            }
        } catch (Exception exception) {
            log.error(exception.toString());
        }

        return stocks;
    }
}

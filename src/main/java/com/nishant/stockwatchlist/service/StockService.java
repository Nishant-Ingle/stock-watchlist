package com.nishant.stockwatchlist.service;

import com.nishant.stockwatchlist.helper.CSVHelper;
import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    @PostConstruct
    public void setup() {
        List<Stock> stocks = CSVHelper.getStockData("static/stock-data.csv");

        if (stockRepository.findAll().isEmpty()) {
            stockRepository.saveAll(stocks);
        }
    }

    public List<Stock> getStocks(String search) {
        if (!StringUtils.isAllBlank(search)) {
            final String searchLowerCase = search.toLowerCase();

            return stockRepository.findAll()
                           .stream()
                           .filter(stock -> stock.getName().toLowerCase().contains(searchLowerCase) ||
                                            stock.getSymbol().toLowerCase().contains(searchLowerCase))
                           .toList();
        }
        return stockRepository.findAll();

//        return stockMap.values().stream().toList();
    }

    public List<Stock> getStocksFromSym(List<String> stockSyms) {
        return stockSyms.stream().map(sym -> stockRepository.findStockBySymbol(sym)).toList();
    }
}

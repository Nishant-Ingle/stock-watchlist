package com.nishant.stockwatchlist.service;

import com.nishant.stockwatchlist.helper.CSVHelper;
import com.nishant.stockwatchlist.model.Stock;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StockService {
    private Map<String, Stock> stockMap = new HashMap<>();

    @PostConstruct
    public void setup() {
        List<Stock> stocks = CSVHelper.getStockData("static/stock-data.csv");
        for (Stock s: stocks) {
            stockMap.put(s.getSymbol(), s);
        }
    }

    public List<Stock> getStocks(String search) {
        if (!StringUtils.isAllBlank(search)) {
            final String searchLowerCase = search.toLowerCase();

            return stockMap.values()
                           .stream()
                           .filter(stock -> stock.getName().toLowerCase().contains(searchLowerCase) ||
                                            stock.getSymbol().toLowerCase().contains(searchLowerCase))
                           .toList();
        }

        return stockMap.values().stream().toList();
    }

    public List<Stock> getStocksFromSym(List<String> stockSyms) {
        return stockSyms.stream().map(sym -> stockMap.getOrDefault(sym, null)).toList();
    }
}

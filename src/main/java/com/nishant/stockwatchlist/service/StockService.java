package com.nishant.stockwatchlist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nishant.stockwatchlist.helper.CSVHelper;
import com.nishant.stockwatchlist.model.Quote;
import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.repository.StockRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StockService {
    @Autowired
    private StockRepository stockRepository;

    // FIXME: Make baseURL a property.
    private String baseURL = "https://www.alphavantage.co";

    private WebClient stockDataClient = WebClient.builder().baseUrl(baseURL).build();

    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Value("${stock.watchlist.allow.quote.fetch: false}")
    private Boolean enableQuoteFetch;

    /**
     * Populate the list of stocks upon start.
     */
    @PostConstruct
    public void setup() {
        List<Stock> stocks = CSVHelper.getStockData("static/stock-data.csv");

        if (stockRepository.findAll().isEmpty()) {
            stockRepository.saveAll(stocks);
        }
    }

    /**
     * Updates prices of all stocks if quote fetch is enabled.
     */
    public void updatePrices() {
        if (!enableQuoteFetch) {
            return;
        }

        List<Stock> stocks = stockRepository.findAll();

        for (Stock stock: stocks) {
            String symbol = stock.getSymbol();

            // FIXME: Store Api key securely than hardcoding.
            String response =
                    stockDataClient.get()
                            .uri("query?function=GLOBAL_QUOTE&symbol=%s&apikey=VU78D3JM5N6KGOS3".formatted(symbol))
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();

            try {
                Map<String, Quote> globalQuote = objectMapper.readValue(response, new TypeReference<>() {});
                stock.setPrice(globalQuote.get("Global Quote").getPrice());

//                Yahoo finance is not supported but usage would have been as follows.
//                stock.setPrice(YahooFinance.get(symbol).getQuote().getPrice().doubleValue());
            } catch (Exception e) {
                log.error(e.toString());
            }
        }

        // Save stocks with updated prices in DB.
        stockRepository.saveAll(stocks);
    }

    /**
     * Get list of stocks.
     *
     * @param search Search term which can either be a substring of company name or symbol.
     * @return List of stocks.
     */
    public List<Stock> getStocks(String search) {
        // Fetch prices using the stock data API.
        updatePrices();

        if (!StringUtils.isAllBlank(search)) {
            final String searchLowerCase = search.toLowerCase();

            return stockRepository.findAll()
                           .stream()
                           .filter(stock -> stock.getName().toLowerCase().contains(searchLowerCase) ||
                                            stock.getSymbol().toLowerCase().contains(searchLowerCase))
                           .toList();
        }
        return stockRepository.findAll();
    }

    public List<Stock> getStocksFromSym(List<String> stockSyms) {
        updatePrices();
        return stockSyms.stream().map(sym -> stockRepository.findStockBySymbol(sym)).toList();
    }
}

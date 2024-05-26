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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        Set<Stock> stocks = CSVHelper.getStockData("static/stock-data.csv");

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

        Set<Stock> stocks = new HashSet<>(stockRepository.findAll());

        for (Stock stock: stocks) {
            String symbol = stock.getSymbol();

            log.info("Fetching stock quotes from Alpha Vantage for symbol {}.", symbol);

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
    public Set<Stock> getStocks(String search) {
        if (StringUtils.isAllBlank(search)) {
            return Set.of();
        }

        final String searchLowerCase = search.toLowerCase();

        return getAllStocks()
                .stream()
                .filter(stock -> stock.getName().toLowerCase().contains(searchLowerCase) ||
                                 stock.getSymbol().toLowerCase().contains(searchLowerCase))
                .collect(Collectors.toSet());
    }

    public Set<Stock> getAllStocks() {
        // Fetch prices using the stock data API.
        updatePrices();
        return new HashSet<>(stockRepository.findAll());
    }

    public Set<String> getAllStockSyms() {
        return getAllStocks().stream().map(stock -> stock.getSymbol()).collect(Collectors.toSet());
    }

    public Set<Stock> getStocksFromSym(Set<String> stockSyms) {
        // todo: update to refresh only for stockSyms
        updatePrices();
        return stockSyms.stream().map(sym -> stockRepository.findStockBySymbol(sym)).collect(Collectors.toSet());
    }
}

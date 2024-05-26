package com.nishant.stockwatchlist.controller;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    /**
     * Get list of stocks.
     * @param search Search term which can either a substring of be symbol or company name.
     * @return List of stocks.
     */
    @GetMapping
    public Set<Stock> getStocks(@RequestParam(required = false) String search) {
        log.info("Received request to get stocks with search: {}.", search);
        Set<Stock> stocks = stockService.getStocks(search);
        log.info("Sending stocks of size: {}.", stocks.size());
        return stocks;
    }
}

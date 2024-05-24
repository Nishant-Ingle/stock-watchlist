package com.nishant.stockwatchlist.controller;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public List<Stock> getStocks(@RequestParam(required = false) String search) {
        List<Stock> stocks = stockService.getStocks(search);
        return stocks;
    }
}

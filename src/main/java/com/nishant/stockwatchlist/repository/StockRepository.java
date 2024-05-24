package com.nishant.stockwatchlist.repository;

import com.nishant.stockwatchlist.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StockRepository extends JpaRepository<Stock, UUID> {
    Stock findStockBySymbol(String symbol);
}

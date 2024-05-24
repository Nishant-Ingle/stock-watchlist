package com.nishant.stockwatchlist.service;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.model.Watchlist;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class WatchlistService {

    @Autowired
    StockService stockService;

    private Map<UUID, Watchlist> watchlistMap = new HashMap<>();

    public List<Watchlist> getWatchlists() {
        return watchlistMap.values().stream().toList();
    }

    public Watchlist getWatchlist(UUID watchlistId) {
        return watchlistMap.get(watchlistId);
    }

    public List<Stock> getWatchlistStocks(UUID watchlistId) {
        return stockService.getStocksFromSym(getWatchlist(watchlistId).getStockSyms());
    }


    public List<UUID> createWatchlists(List<Watchlist> watchlists) {
        List<UUID> addedWatchlists = new ArrayList<>();

        for (Watchlist watchlist: watchlists) {
            String name = watchlist.getName();
            UUID owner = watchlist.getOwner();
            List<String> stockSyms  = watchlist.getStockSyms();
            UUID watchlistId = UUID.randomUUID();
            watchlistMap.put(watchlistId, new Watchlist(watchlistId, name, owner, stockSyms));
            addedWatchlists.add(watchlistId);
        }

        return addedWatchlists;
    }

    public void addStocks(UUID watchlistId, List<String> stocks) {
        log.info("Adding {} stocks to {}", stocks.size(), watchlistId);
        watchlistMap.get(watchlistId).getStockSyms().addAll(stocks);
    }

    public void deleteStocks(UUID watchlistId, List<String> stockSym) {
        Set<String> stockSet = new HashSet<>(stockSym);

        log.info("Deleting {} stocks to {}", stockSym.size(), watchlistId);
        watchlistMap.get(watchlistId).getStockSyms().removeIf(stockSet::contains);
    }
}

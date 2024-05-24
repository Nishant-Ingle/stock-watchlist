package com.nishant.stockwatchlist.service;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.model.Watchlist;
import com.nishant.stockwatchlist.repository.WatchlistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class WatchlistService {
    @Autowired
    private WatchlistRepository watchlistRepository;

    @Autowired
    private StockService stockService;

    public List<Watchlist> getWatchlists() {
        return watchlistRepository.findAll();
    }

    public Watchlist getWatchlist(UUID watchlistId) {
        return watchlistRepository.findWatchlistById(watchlistId);
    }

    public List<Stock> getWatchlistStocks(UUID watchlistId) {
        return stockService.getStocksFromSym(getWatchlist(watchlistId).getStockSyms());
    }


    public List<UUID> createWatchlists(List<Watchlist> watchlists) {
        watchlists = watchlistRepository.saveAll(watchlists);

        return watchlists.stream().map(Watchlist::getId).toList();
    }

    public void addStocks(UUID watchlistId, List<String> stocks) {
        log.info("Adding {} stocks to {}", stocks.size(), watchlistId);
        var watchlist = watchlistRepository.findWatchlistById(watchlistId);
        watchlist.getStockSyms().addAll(stocks);
        watchlistRepository.save(watchlist);
    }

    public void deleteStocks(UUID watchlistId, List<String> stockSym) {
        Set<String> stockSet = new HashSet<>(stockSym);

        log.info("Deleting {} stocks from {}", stockSym.size(), watchlistId);
        var watchlist = watchlistRepository.findWatchlistById(watchlistId);
        watchlist.getStockSyms().removeIf(stockSet::contains);
        watchlistRepository.save(watchlist);;
    }
}

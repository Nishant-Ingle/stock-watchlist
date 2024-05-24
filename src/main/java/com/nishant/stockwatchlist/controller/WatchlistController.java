package com.nishant.stockwatchlist.controller;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.model.Watchlist;
import com.nishant.stockwatchlist.service.WatchlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/watchlists")
public class WatchlistController {
    @Autowired
    private WatchlistService watchlistService;

    @GetMapping
    public ResponseEntity<List<Watchlist>> getWatchLists() {
        return ResponseEntity.ok(watchlistService.getWatchlists());
    }

    @GetMapping("/{watchlistId}")
    public ResponseEntity<Watchlist> getWatchLists(@PathVariable UUID watchlistId) {
        return ResponseEntity.ok(watchlistService.getWatchlist(watchlistId));
    }

    @GetMapping("/{watchlistId}/stocks")
    public ResponseEntity<List<Stock>> getWatchListStocks(@PathVariable UUID watchlistId) {
        List<Stock> watchlistStocks = watchlistService.getWatchlistStocks(watchlistId);
        return ResponseEntity.ok().body(watchlistStocks);
    }

    @PostMapping()
    public ResponseEntity<List<UUID>> createWatchlists(@RequestBody List<Watchlist> watchlists) {
        List<UUID> watchlistIds = watchlistService.createWatchlists(watchlists);
        return ResponseEntity.status(HttpStatus.CREATED).body(watchlistIds);
    }

    @PatchMapping("/{watchListId}/stocks")
    public ResponseEntity<?> addStocksToWatchlist(
            @PathVariable UUID watchListId,
            @RequestBody List<String> stockSym)
    {
        watchlistService.addStocks(watchListId, stockSym);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{watchListId}/stocks")
    public ResponseEntity<?> deleteStocksToWatchlist(
            @PathVariable UUID watchListId,
            @RequestBody List<String> stockSyms)
    {
        watchlistService.deleteStocks(watchListId, stockSyms);
        return ResponseEntity.ok().build();
    }
}

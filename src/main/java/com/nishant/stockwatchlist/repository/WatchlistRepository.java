package com.nishant.stockwatchlist.repository;

import com.nishant.stockwatchlist.model.Stock;
import com.nishant.stockwatchlist.model.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {
    Watchlist findWatchlistById(UUID id);
}

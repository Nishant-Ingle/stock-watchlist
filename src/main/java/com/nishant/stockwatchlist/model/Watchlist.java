package com.nishant.stockwatchlist.model;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Watchlist {

    // Unique identifier for a watchlist.
    private UUID id;

    private final String name;

    // Customer ID that owns this watchlist.
    private final UUID owner;

    // todo change to uuid
    private final List<String> stockSyms;
}

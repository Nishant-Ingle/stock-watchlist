package com.nishant.stockwatchlist.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName="of")
@Entity
@Data
public class Watchlist {

    // Unique identifier for a watchlist.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private final String name;

    // Customer ID that owns this watchlist.
    private final UUID owner;

    // todo change to uuid
    @ElementCollection
    private final List<String> stockSyms;
}

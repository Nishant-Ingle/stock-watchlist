package com.nishant.stockwatchlist.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

// TODO: Have different set of controller-service models, service-persistence
//  and set of REST API models for better security.
@Data
public class Stock {

    // Unique identifier for a stock.
    private final UUID id;

    private final String symbol;

    // Name of the company
    private final String name;

    private final double price;
}

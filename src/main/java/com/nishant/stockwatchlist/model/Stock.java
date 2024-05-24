package com.nishant.stockwatchlist.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

// TODO: Have different set of controller-service models, service-persistence
//  and set of REST API models for better security.
@NoArgsConstructor(force = true)
@RequiredArgsConstructor(staticName="of")
@Entity
@Data
public class Stock {

    // Unique identifier for a stock.
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private final String symbol;

    // Name of the company
    private final String name;

    private double price;
}

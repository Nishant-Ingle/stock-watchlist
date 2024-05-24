package com.nishant.stockwatchlist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class Quote {
    @JsonProperty("01. symbol")
    private String symbol;

    @JsonProperty("05. price")
    private Double price;
}


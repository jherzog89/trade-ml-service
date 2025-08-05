package com.herzog.trading.trade_ml_service.model;


import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MarketDataModel {

    @Id
    private Long id;
    private String symbol;
    private double price;
    private long volume;
    private Instant timestamp;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public long getVolume() { return volume; }
    public void setVolume(long volume) { this.volume = volume; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
package com.herzog.trading.trade_ml_service.service;

import org.springframework.stereotype.Service;

import com.herzog.trading.trade_ml_service.model.MarketDataModel;

import java.util.List;

@Service
public class FeatureEngineeringService {

    public double[] computeFeatures(List<MarketDataModel> data) {
        // Compute 50-day SMA
        double sma50 = computeSMA(data, 50);
        // Compute 200-day SMA
        double sma200 = computeSMA(data, 200);
        // Compute 14-day RSI
        double rsi = computeRSI(data, 14);
        // Latest volume
        double volume = data.get(data.size() - 1).getVolume();

        return new double[]{sma50, sma200, rsi, volume};
    }

    private double computeSMA(List<MarketDataModel> data, int period) {
        if (data.size() < period) return 0.0;
        double sum = 0.0;
        for (int i = data.size() - period; i < data.size(); i++) {
            sum += data.get(i).getPrice();
        }
        return sum / period;
    }

    private double computeRSI(List<MarketDataModel> data, int period) {
        if (data.size() < period) return 0.0;
        double gains = 0.0, losses = 0.0;
        for (int i = data.size() - period; i < data.size() - 1; i++) {
            double change = data.get(i + 1).getPrice() - data.get(i).getPrice();
            if (change > 0) gains += change;
            else losses -= change;
        }
        double avgGain = gains / period;
        double avgLoss = losses / period;
        if (avgLoss == 0) return 100;
        double rs = avgGain / avgLoss;
        return 100 - (100 / (1 + rs));
    }
}
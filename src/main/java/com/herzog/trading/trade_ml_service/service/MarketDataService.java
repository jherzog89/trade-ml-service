package com.herzog.trading.trade_ml_service.service;

import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.util.apitype.MarketDataWebsocketSourceType;
import net.jacobpeterson.alpaca.model.util.apitype.TraderAPIEndpointType;
import net.jacobpeterson.alpaca.openapi.marketdata.ApiException;
import net.jacobpeterson.alpaca.openapi.marketdata.model.Sort;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockAdjustment;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockBar;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockBarsRespSingle;
import net.jacobpeterson.alpaca.openapi.marketdata.model.StockFeed;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.herzog.trading.trade_ml_service.model.MarketDataModel;

import jakarta.annotation.PostConstruct;
/**
 * Service to fetch market data from Alpaca API.
 * This service retrieves stock bars for a specified symbol and timeframe,
 * processes the data, and stores it in a list of MarketDataModel.
 * 
 * Note: The actual model loading and prediction logic is not implemented here.
 * This is a placeholder for future integration with a machine learning model.
 */
@Service
public class MarketDataService {

    @Value("${alpaca.api.key}")
    private String keyID;

    @Value("${alpaca.api.secret.key}")
    private String secretKey;

    @Value("${alpaca.api.symbol}")
    private String symbol;

    @PostConstruct
    public void init() {
        this.alpacaAPI = new AlpacaAPI(keyID, secretKey, TraderAPIEndpointType.PAPER, MarketDataWebsocketSourceType.IEX);
        fetchMarketData();
    }

    private AlpacaAPI alpacaAPI;

    private List<MarketDataModel> marketDataList;

    public MarketDataService() {
        // Initialize Alpaca API with credentials and endpoint type
        //this.alpacaAPI = new AlpacaAPI(keyID, secretKey, TraderAPIEndpointType.PAPER, MarketDataWebsocketSourceType.IEX);
        //fetchMarketData();
    }

    public void fetchMarketData() {

        // Define the parameters for the stock bars request
            //String symbol = "AAPL"; // Stock symbol (e.g., Apple)
            String timeframe = "5Min"; // Timeframe for the 
           // OffsetDateTime start = OffsetDateTime.of(2025, 8, 5, 9, 30, 0, 0, ZoneOffset.UTC); // Start time
            //OffsetDateTime end = OffsetDateTime.of(2025, 8, 5, 16, 0, 0, 0, ZoneOffset.UTC); // End time
            OffsetDateTime end = OffsetDateTime.now(ZoneOffset.UTC); // Current time in UTC
            OffsetDateTime start = end.minusHours(4); // 4 hours ago from current time
            Long limit = 10L; // Limit on number of bars to retrieve
            StockAdjustment adjustment = StockAdjustment.RAW; // No dividend/split adjustments
            String pageToken =  null; // For pagination, null for first request
            StockFeed feed = StockFeed.IEX; // Data feed 
            String exchange = null; // Specific exchange (null for all)
            String currency = "USD"; // Currency for the data
            Sort sort = Sort.ASC; // Sort order (ascending)
  

        // Retrieve stock bars
        try {
           StockBarsRespSingle bars = alpacaAPI.marketData().stock().stockBarSingle(symbol,
                timeframe,
                start,
                end,
                limit,
                adjustment,
                exchange,
                feed,
                currency,
                pageToken,
                sort);
            marketDataList = new ArrayList<MarketDataModel>();
           for(StockBar bar : bars.getBars()){
                MarketDataModel marketData = new MarketDataModel();
                marketData.setSymbol(symbol);
                marketData.setPrice(bar.getC());
                marketData.setVolume(bar.getV());
                marketData.setTimestamp(bar.getT().toInstant());
           }
            
        } catch (ApiException e) {
            e.printStackTrace();
        }

    }
}
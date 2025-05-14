package tr.com.muskar.crypto_analysis_bot.model;

import lombok.Data;

@Data
public class FeatureVector {

    private double rsi;
    private double macd;
    private double macdSignal;
    private double bollingerPercentB;
    private double priceChangePercent;

    @Override
    public String toString() {
        return "FeatureVector{" +
                "rsi=" + rsi +
                ", macd=" + macd +
                ", macdSignal=" + macdSignal +
                ", bollingerPercentB=" + bollingerPercentB +
                ", priceChangePercent=" + priceChangePercent +
                '}';
    }
}

package tr.com.muskar.crypto_analysis_bot.signal;

import org.springframework.stereotype.Component;
import tr.com.muskar.crypto_analysis_bot.model.FeatureVector;
import tr.com.muskar.crypto_analysis_bot.signal.enums.SignalType;

@Component
public class SignalEvaluator {




    public SignalType evaluate(double price, double rsi, double macd, double[] bb) {
        boolean rsiBuy = rsi < 30;
        boolean rsiSell = rsi > 70;
        boolean macdBuy = macd > 0;
        boolean macdSell = macd < 0;
        boolean bbBuy = price < bb[0];
        boolean bbSell = price > bb[2];

        int buyScore = 0;
        int sellScore = 0;

        if (rsiBuy) buyScore++;
        if (macdBuy) buyScore++;
        if (bbBuy) buyScore++;

        if (rsiSell) sellScore++;
        if (macdSell) sellScore++;
        if (bbSell) sellScore++;

        if (buyScore >= 2) {
            return SignalType.BUY;
        } else if (sellScore >= 2) {
            return SignalType.SELL;
        } else {
            return SignalType.HOLD;
        }
    }

    public SignalType evaluate(FeatureVector vector) {
        int score = 0;

        if (vector.getRsi() < 30) score += 2;
        else if (vector.getRsi() > 70) score -= 2;

        if (vector.getMacd() > vector.getMacdSignal()) score += 1;
        else score -= 1;

        if (vector.getBollingerPercentB() < 0.1) score += 1;
        else if (vector.getBollingerPercentB() > 0.9) score -= 1;

        if (vector.getPriceChangePercent() < -3) score += 1;
        else if (vector.getPriceChangePercent() > 3) score -= 1;

        if (score >= 3) return SignalType.BUY;
        else if (score <= -3) return SignalType.SELL;
        else return SignalType.HOLD;
    }
}


package tr.com.muskar.crypto_analysis_bot.service;

import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.bollinger.*;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import tr.com.muskar.crypto_analysis_bot.model.FeatureVector;

@Service
public class FeatureEngineeringService {

    public FeatureVector extractFeatures(BarSeries series) {
        int endIndex = series.getEndIndex();
        if (endIndex < 20) return null;

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        RSIIndicator rsi = new RSIIndicator(closePrice, 14);
        double rsiValue = rsi.getValue(endIndex).doubleValue();


        MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator macdSignal = new EMAIndicator(macd, 9);
        double macdValue = macd.getValue(endIndex).doubleValue();
        double macdSignalValue = macdSignal.getValue(endIndex).doubleValue();


        BollingerBandsMiddleIndicator bbm = new BollingerBandsMiddleIndicator(closePrice);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(closePrice, 20);
        BollingerBandsUpperIndicator bbu = new BollingerBandsUpperIndicator(bbm, stdDev);
        BollingerBandsLowerIndicator bbl = new BollingerBandsLowerIndicator(bbm, stdDev);

        double close = closePrice.getValue(endIndex).doubleValue();
        double upper = bbu.getValue(endIndex).doubleValue();
        double lower = bbl.getValue(endIndex).doubleValue();
        double percentB = (close - lower) / (upper - lower);


        double prevClose = closePrice.getValue(endIndex - 1).doubleValue();
        double priceChangePct = ((close - prevClose) / prevClose) * 100;


        FeatureVector vector = new FeatureVector();
        vector.setRsi(rsiValue);
        vector.setMacd(macdValue);
        vector.setMacdSignal(macdSignalValue);
        vector.setBollingerPercentB(percentB);
        vector.setPriceChangePercent(priceChangePct);

        return vector;
    }
}

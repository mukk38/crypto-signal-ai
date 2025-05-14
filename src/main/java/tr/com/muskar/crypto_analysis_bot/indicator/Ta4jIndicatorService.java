package tr.com.muskar.crypto_analysis_bot.indicator;


import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import org.ta4j.core.num.Num;
import org.ta4j.core.indicators.RSIIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.BaseBar;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class Ta4jIndicatorService {

    public double calculateRSI(List<Double> closePrices) {
        BarSeries series = createBarSeries(closePrices);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        RSIIndicator rsi = new RSIIndicator(closePrice, 14);
        return rsi.getValue(series.getEndIndex()).doubleValue();
    }

    public double calculateMACD(List<Double> closePrices) {
        BarSeries series = createBarSeries(closePrices);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
        EMAIndicator signalLine = new EMAIndicator(macd, 9);
        Num macdValue = macd.getValue(series.getEndIndex());
        Num signalValue = signalLine.getValue(series.getEndIndex());
        return macdValue.minus(signalValue).doubleValue();
    }

    public double[] calculateBollingerBands(List<Double> closePrices) {
        BarSeries series = createBarSeries(closePrices);
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(closePrice);
        BollingerBandsUpperIndicator upperBB = new BollingerBandsUpperIndicator(middleBB, closePrice, DecimalNum.valueOf(2));
        BollingerBandsLowerIndicator lowerBB = new BollingerBandsLowerIndicator(middleBB, closePrice, DecimalNum.valueOf(2));

        int end = series.getEndIndex();

        return new double[] {
                lowerBB.getValue(end).doubleValue(),
                middleBB.getValue(end).doubleValue(),
                upperBB.getValue(end).doubleValue()
        };
    }

    public BarSeries createBarSeries(List<Double> closePrices) {
        BarSeries series = new BaseBarSeriesBuilder().withName("crypto_series").build();
        ZonedDateTime endTime = ZonedDateTime.now().minusMinutes(closePrices.size());
        for (double price : closePrices) {
            endTime = endTime.plusMinutes(1);
            series.addBar(BaseBar.builder()
                    .timePeriod(java.time.Duration.ofHours(1))
                    .endTime(endTime)
                    .openPrice(DecimalNum.valueOf(price))
                    .highPrice(DecimalNum.valueOf(price))
                    .lowPrice(DecimalNum.valueOf(price))
                    .closePrice(DecimalNum.valueOf(price))
                    .volume(DecimalNum.valueOf(1))
                    .build()
            );
        }

        return series;
    }

    public BarSeries createBarSeriesFromClosingPrices(List<Double> closingPrices) {
        BarSeries series = new BaseBarSeriesBuilder().withName("backtest").build();
        for (Double price : closingPrices) {
            series.addBar(BaseBar.builder()
                    .timePeriod(Duration.ofDays(1))
                    .endTime(ZonedDateTime.now())
                    .openPrice(DecimalNum.valueOf(price))
                    .highPrice(DecimalNum.valueOf(price))
                    .lowPrice(DecimalNum.valueOf(price))
                    .closePrice(DecimalNum.valueOf(price))
                    .volume(DecimalNum.valueOf(0.0))
                    .build());
        }
        return series;
    }

    public double getRSI(BarSeries series, int index) {
        RSIIndicator rsi = new RSIIndicator(new ClosePriceIndicator(series), 14);
        return rsi.getValue(index).doubleValue();
    }

    public double getMACD(BarSeries series, int index) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        MACDIndicator macd = new MACDIndicator(closePrice, 12, 26);
        return macd.getValue(index).doubleValue();
    }

    public double[] getBollingerBandScore(BarSeries series, int index) {
        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);
        SMAIndicator sma = new SMAIndicator(closePrice, 20);
        StandardDeviationIndicator stdDev = new StandardDeviationIndicator(closePrice, 20);

        BollingerBandsMiddleIndicator middleBB = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator upperBB = new BollingerBandsUpperIndicator(middleBB, stdDev);
        BollingerBandsLowerIndicator lowerBB = new BollingerBandsLowerIndicator(middleBB, stdDev);

        double close = closePrice.getValue(index).doubleValue();
        double lower = lowerBB.getValue(index).doubleValue();
        double upper = upperBB.getValue(index).doubleValue();

        return new double[]{close, lower, upper};
    }

}

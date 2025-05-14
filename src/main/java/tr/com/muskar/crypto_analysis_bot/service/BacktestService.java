package tr.com.muskar.crypto_analysis_bot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.ta4j.core.BarSeries;
import smile.classification.RandomForest;
import tr.com.muskar.crypto_analysis_bot.dataGenerator.CsvDatasetGenerator;
import tr.com.muskar.crypto_analysis_bot.fetcher.CoinGeckoClient;
import tr.com.muskar.crypto_analysis_bot.indicator.Ta4jIndicatorService;
import tr.com.muskar.crypto_analysis_bot.model.FeatureVector;
import tr.com.muskar.crypto_analysis_bot.signal.SignalEvaluator;
import tr.com.muskar.crypto_analysis_bot.signal.enums.SignalType;
import tr.com.muskar.crypto_analysis_bot.simulator.TradeSimulator;
import tr.com.muskar.crypto_analysis_bot.trader.MLModelEvaluator;
import tr.com.muskar.crypto_analysis_bot.trader.MLTrainer;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BacktestService {

    private final CoinGeckoClient client;
    private final Ta4jIndicatorService indicatorService;
    private final SignalEvaluator evaluator;
    private final TradeSimulator tradeSimulator;





    public void runBacktest(String coinId, int days) {
        List<Double> closePrices = client.getHistoricalClosingPrices(coinId, days);
        if (closePrices.size() < 20) {
            System.out.println("Veri yetersiz.");
            return;
        }

        var series = indicatorService.createBarSeriesFromClosingPrices(closePrices);

        for (int i = 14; i < series.getBarCount(); i++) {
            double rsi = indicatorService.getRSI(series, i);
            double macd = indicatorService.getMACD(series, i);
            double[] bb = indicatorService.getBollingerBandScore(series, i);
            double currentPrice = series.getBar(i).getClosePrice().doubleValue();

            SignalType signal = evaluator.evaluate(currentPrice,rsi, macd, bb);
            tradeSimulator.handleSignal(coinId, signal, currentPrice);
        }

        tradeSimulator.printTradeSummary();
    }


}

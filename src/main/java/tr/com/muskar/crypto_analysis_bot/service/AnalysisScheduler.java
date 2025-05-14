package tr.com.muskar.crypto_analysis_bot.service;


import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import smile.classification.RandomForest;
import tr.com.muskar.crypto_analysis_bot.dataGenerator.CsvDatasetGenerator;
import tr.com.muskar.crypto_analysis_bot.fetcher.CoinGeckoClient;
import tr.com.muskar.crypto_analysis_bot.indicator.Ta4jIndicatorService;
import tr.com.muskar.crypto_analysis_bot.model.FeatureVector;
import tr.com.muskar.crypto_analysis_bot.model.entity.TradeRecord;
import tr.com.muskar.crypto_analysis_bot.notifier.MailNotifier;
import tr.com.muskar.crypto_analysis_bot.notifier.TelegramNotifier;
import tr.com.muskar.crypto_analysis_bot.repository.TradeRepository;
import tr.com.muskar.crypto_analysis_bot.signal.SignalEvaluator;
import tr.com.muskar.crypto_analysis_bot.signal.enums.SignalType;
import tr.com.muskar.crypto_analysis_bot.simulator.TradeSimulator;
import tr.com.muskar.crypto_analysis_bot.trader.MLModelEvaluator;
import tr.com.muskar.crypto_analysis_bot.trader.MLTrainer;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalysisScheduler {

    private final CoinGeckoClient fetcher;
    private final Ta4jIndicatorService indicator;
    private final SignalEvaluator evaluator;
    private final MailNotifier mailNotifier;
    private final TelegramNotifier telegramNotifier;
    private final TradeSimulator tradeSimulator;
    private final TradeRepository tradeRepository;
    private final List<String> getCoins;
    private final FeatureEngineeringService featureEngineeringService;
    private final CsvDatasetGenerator csvDatasetGenerator;
    private final MLTrainer mlTrainer = new MLTrainer();


    @Value("${csv.name}")
    private String csvName;


    @Scheduled(fixedRate = 300000)
    public void analyze() {
        String symbol = "bitcoin";
        List<Double> closePrices = fetcher.getLast14ClosePrices(symbol);
        double currentPrice = closePrices.get(closePrices.size() - 1);
        double rsi = indicator.calculateRSI(closePrices);

        double macd = indicator.calculateMACD(closePrices);
        double[] bb = indicator.calculateBollingerBands(closePrices);
        double lower = bb[0], middle = bb[1], upper = bb[2];
        SignalType signal = evaluator.evaluate(currentPrice, rsi, macd, bb);
        switch (signal) {
            case BUY:
                System.out.println(" AL Sinyali");
                break;
            case SELL:
                System.out.println(" SAT Sinyali");
                break;
            case HOLD:
                System.out.println("⏸ BEKLE");
                break;
        }


        if (signal != SignalType.HOLD) {
            mailNotifier.sendSignal(symbol, signal.name(), currentPrice);
            telegramNotifier.sendSignal(symbol, signal.name(), currentPrice);

            TradeRecord tradeRecord = new TradeRecord(symbol, symbol, currentPrice, Instant.now().toString(), "pending");
            tradeRepository.save(tradeRecord);
        }
        tradeSimulator.handleSignal(symbol, signal, currentPrice);
    }

    @PreDestroy
    public void onExit() {
        tradeSimulator.printTradeSummary();
    }
    @Scheduled(fixedRate = 300000)
    public void runBacktest() {
        for (String coin : getCoins) {
            System.out.println("Backtesting for: " + coin);
            List<Double> prices = fetcher.getHistoricalClosingPrices(coin,  5);
            double currentPrice = prices.get(prices.size() - 1);
            double rsi = indicator.calculateRSI(prices);

            double macd = indicator.calculateMACD(prices);
            double[] bb = indicator.calculateBollingerBands(prices);
            double lower = bb[0], middle = bb[1], upper = bb[2];
            SignalType signal = evaluator.evaluate(currentPrice, rsi, macd, bb);
            tradeSimulator.handleSignal(coin,signal, currentPrice);
            System.out.println("Results for " + coin + ": ");
            tradeSimulator.printTradeSummary();
        }
    }

    @Scheduled(fixedRate = 300000)
    public void featuringEngineeringBusiness(){
        for (String coin : getCoins) {
            List<Double> closePrices = fetcher.getLast14ClosePrices(coin);
            BarSeries barSeries= indicator.createBarSeriesFromClosingPrices(closePrices);
            FeatureVector featureVector= featureEngineeringService.extractFeatures(barSeries);
            System.out.println(featureVector.toString());
        }
    }
    @Scheduled(fixedRate = 1000000)
    public void generateCsv(){
        String coinId = "bitcoin";
        String currency = "usd";
        int hours = 6;

        try {
            List<Double> closePrices = fetcher.getHistoricalClosePrices(coinId, currency, hours);
            csvDatasetGenerator.generateDataset(closePrices, csvName);
            System.out.println("CSV dosyası oluşturuldu: " + csvName);
            RandomForest randomForest= mlTrainer.trainModel(csvName);
            MLModelEvaluator mlModelEvaluator = new MLModelEvaluator(randomForest);
            BarSeries barSeries= indicator.createBarSeriesFromClosingPrices(closePrices);
            FeatureVector featureVector= featureEngineeringService.extractFeatures(barSeries);
            mlModelEvaluator.predict(featureVector);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}


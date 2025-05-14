package tr.com.muskar.crypto_analysis_bot.simulator;

import org.springframework.stereotype.Component;
import tr.com.muskar.crypto_analysis_bot.model.Position;
import tr.com.muskar.crypto_analysis_bot.model.Trade;
import tr.com.muskar.crypto_analysis_bot.signal.SignalEvaluator;
import tr.com.muskar.crypto_analysis_bot.signal.enums.SignalType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class TradeSimulator {

    private final List<Trade> tradeHistory = new ArrayList<>();
    private final Position position = new Position();
    private double totalProfit = 0.0;

    public void handleSignal(String coin, SignalType signal, double price) {
        LocalDateTime now = LocalDateTime.now();

        switch (signal) {
            case BUY -> {
                if (!position.isOpen()) {
                    position.open(price);
                    tradeHistory.add(new Trade(coin, "BUY", price, now));
                    System.out.println(" BUY emri simüle edildi - $" + price);
                }
            }
            case SELL -> {
                if (position.isOpen()) {
                    double profit = position.close(price);
                    totalProfit += profit;
                    tradeHistory.add(new Trade(coin, "SELL", price, now));
                    System.out.println(" SELL emri simüle edildi - $" + price + " | Kâr: " + profit);
                }
            }
            case HOLD -> {
                // No action
            }
        }
    }

    public void printTradeSummary() {
        System.out.println(" Trade geçmişi:");
        tradeHistory.forEach(System.out::println);
        System.out.println(" Toplam Kar/Zarar: $" + totalProfit);
        tradeHistory.clear();
    }
}


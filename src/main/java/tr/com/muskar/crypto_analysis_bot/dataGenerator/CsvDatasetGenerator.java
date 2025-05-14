package tr.com.muskar.crypto_analysis_bot.dataGenerator;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.ta4j.core.BarSeries;
import tr.com.muskar.crypto_analysis_bot.indicator.Ta4jIndicatorService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CsvDatasetGenerator {

    private final Ta4jIndicatorService indicatorService;

    public void generateDataset(List<Double> closePrices, String filePath) throws IOException {
        FileWriter csvWriter = new FileWriter(filePath);
        csvWriter.append("timestamp,rsi,macd,bb_score,price_change_1h,label\n");

        for (int i = 30; i < closePrices.size() - 12; i++) {
            List<Double> subList = closePrices.subList(i - 14, i);
            BarSeries series = indicatorService.createBarSeries(subList);

            double rsi = indicatorService.getRSI(series,i);
            double macd = indicatorService.getMACD(series,i);
            double[] bb = indicatorService.getBollingerBandScore(series,i);
            double currentPrice = closePrices.get(i);
            double futurePrice = closePrices.get(i + 12); // 1 saat sonra (varsayım: 5dk aralıkla 12 bar)
            double priceDiff = (futurePrice - currentPrice) / currentPrice;

            int label = 0;
            if (priceDiff > 0.01) label = 1;
            else if (priceDiff < -0.01) label = -1;

            String timestamp = ZonedDateTime.now().minusMinutes((closePrices.size() - i) * 5).toString();

            csvWriter.append(String.format("%s,%.2f,%.4f,%.4f,%.4f,%d\n",
                    timestamp, rsi, macd, bb[0], priceDiff, label));
        }

        csvWriter.flush();
        csvWriter.close();
    }
}

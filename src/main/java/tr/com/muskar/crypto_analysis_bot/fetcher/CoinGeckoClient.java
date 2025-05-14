package tr.com.muskar.crypto_analysis_bot.fetcher;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.litesoftwares.coingecko.CoinGeckoApiClient;
import com.litesoftwares.coingecko.impl.CoinGeckoApiClientImpl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CoinGeckoClient {

    private static final String API_URL = "https://api.coingecko.com/api/v3/coins/markets" +
            "?vs_currency=usd&ids=%s&sparkline=true";
    private final OkHttpClient httpClient = new OkHttpClient();

    private final CoinGeckoApiClient client = new CoinGeckoApiClientImpl();
    private final RestTemplate restTemplate = new RestTemplate();

    public double getPrice(String coinId) {
        Map<String, Map<String, Double>> priceMap = client.getPrice(coinId, "usd");
        return priceMap.get(coinId).get("usd");
    }

    public List<Double> getLast14ClosePrices(String coinId) {
        List<Double> closePrices = new ArrayList<>();

        try {
            String urlStr = String.format(API_URL, coinId);
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            InputStream is = conn.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(is);

            JsonNode prices = root.get(0).get("sparkline_in_7d").get("price");

            int total = prices.size();
            for (int i = total - 14; i < total; i++) {
                closePrices.add(prices.get(i).asDouble());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return closePrices;
    }

    public List<Double> getHistoricalClosingPrices(String coinId, int days) {
        try {
            String url = String.format(
                    "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=usd&days=%d&interval=daily",
                    coinId, days
            );
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);
            List<List<Object>> prices = (List<List<Object>>) response.get("prices");

            return prices.stream()
                    .map(data -> ((Number) data.get(1)).doubleValue()) // sadece kapanış fiyatı
                    .collect(Collectors.toList());

        } catch (Exception e) {
            System.err.println(" Geçmiş fiyatlar çekilemedi: " + e.getMessage());
            return List.of();
        }
    }

    public List<Double> getHistoricalClosePrices(String coinId, String currency, int hoursBack) throws IOException {
        int intervalMinutes = 5;
        int totalPoints = hoursBack * 60 / intervalMinutes;
        String url = String.format(
                "https://api.coingecko.com/api/v3/coins/%s/market_chart?vs_currency=%s&days=1&interval=daily",
                coinId, currency);

        Request request = new Request.Builder().url(url).build();
        Response response = httpClient.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }

        String responseBody = response.body().string();
        JSONObject json = new JSONObject(responseBody);
        JSONArray prices = json.getJSONArray("prices");

        List<Double> closePrices = new ArrayList<>();
        for (int i = prices.length() - totalPoints; i < prices.length(); i++) {
            JSONArray priceEntry = prices.getJSONArray(i);
            double close = priceEntry.getDouble(1);
            closePrices.add(close);
        }

        return closePrices;
    }
}

package tr.com.muskar.crypto_analysis_bot.notifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class TelegramNotifier {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.chat.id}")
    private String chatId;
    private final RestTemplate restTemplate = new RestTemplate();


    public void sendSignal(String coin, String signal, double price) {
        String message = String.format(" %s sinyali geldi!\nCoin: %s\nFiyat: $%.2f\nZaman: %s",
                signal, coin.toUpperCase(), price, java.time.LocalDateTime.now());

        try {
            String url = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                    botToken,
                    chatId,
                    URLEncoder.encode(message, StandardCharsets.UTF_8)
            );
            restTemplate.getForObject(url, String.class);
            System.out.println(" Telegram mesajı gönderildi.");
        } catch (Exception e) {
            System.err.println(" Telegram mesajı gönderilemedi: " + e.getMessage());
        }
    }
}


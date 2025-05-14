package tr.com.muskar.crypto_analysis_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class AppConfig {
    @Bean
    public static List<String> getCoins() {
        return Arrays.asList("bitcoin", "ethereum", "binancecoin", "litecoin","solana");
    }
}

package tr.com.muskar.crypto_analysis_bot.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Trade {

    private String coin;
    private String type;
    private double price;
    private LocalDateTime time;
}

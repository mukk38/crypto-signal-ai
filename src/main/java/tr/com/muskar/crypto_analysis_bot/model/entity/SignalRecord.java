package tr.com.muskar.crypto_analysis_bot.model.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "signal_records")
public class SignalRecord {

    @Id
    private String id;
    private String symbol;
    private String signal;
    private double price;
    private String timestamp;

    public SignalRecord(String symbol, String signal, double price, String timestamp) {
        this.symbol = symbol;
        this.signal = signal;
        this.price = price;
        this.timestamp = timestamp;
    }
}

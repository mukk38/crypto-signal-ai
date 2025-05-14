package tr.com.muskar.crypto_analysis_bot.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trade_records")
public class TradeRecord {

    @Id
    private String id;
    private String symbol;
    private String action;  // AL veya SAT
    private double price;
    private String timestamp;
    private String status;  // completed, pending, etc.

    public TradeRecord(String symbol, String action, double price, String timestamp, String status) {
        this.symbol = symbol;
        this.action = action;
        this.price = price;
        this.timestamp = timestamp;
        this.status = status;
    }
}

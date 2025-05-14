package tr.com.muskar.crypto_analysis_bot.model;

public class Position {

    private boolean open;
    private double entryPrice;

    public Position() {
        this.open = false;
    }

    public void open(double price) {
        this.open = true;
        this.entryPrice = price;
    }

    public double close(double exitPrice) {
        this.open = false;
        return exitPrice - entryPrice;
    }

    public boolean isOpen() {
        return open;
    }

    public double getEntryPrice() {
        return entryPrice;
    }
}

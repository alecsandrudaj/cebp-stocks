package utils;

import java.util.UUID;

public class Transaction {
    private UUID seller, buyer;
    private int shares;
    private double price;

    public Transaction(UUID seller, UUID buyer, int shares, double price) {
        this.seller = seller;
        this.buyer = buyer;
        this.shares = shares;
        this.price = price;
    }

    public Transaction (String data) {
        String[] splitData = data.split(":");
        if (splitData.length == 4) {
            this.seller = UUID.fromString(splitData[0]);
            this.buyer = UUID.fromString(splitData[1]);
            this.shares = Integer.parseInt(splitData[2]);
            this.price = Integer.parseInt(splitData[3]);
        }
        else
            throw new IllegalArgumentException("Incorrect data for order");
    }
    @Override
    public String toString() {
        return seller.toString() + ":" + buyer.toString() + ":" + shares + ":" + price;
    }
}

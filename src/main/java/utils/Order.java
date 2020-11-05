package utils;

import java.util.UUID;

public class Order {
    private int sharesNumber, pricePerAction;
    private UUID clientId;
    private OrderType type;

    public Order(int sharesNumber, int pricePerAction, UUID clientId, OrderType type) {
        this.clientId = clientId;
        this.pricePerAction = pricePerAction;
        this.sharesNumber = sharesNumber;
        this.type = type;
    }

    public enum OrderType {
        BUY, SELL, HISTORY;
    }
}

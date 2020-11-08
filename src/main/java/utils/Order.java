package utils;

import java.util.UUID;

public class Order {
    private int sharesNumber;
    private double pricePerAction;
    private UUID clientId, orderId, sellerId;
    private OrderType type;

    public Order(int sharesNumber, double pricePerAction, UUID clientId) {
        this.clientId = clientId;
        this.pricePerAction = pricePerAction;
        this.sharesNumber = sharesNumber;
        this.type = OrderType.SELL;
        this.orderId = UUID.randomUUID();
        this.sellerId = null;
    }

    public Order(int sharesNumber, double pricePerAction, UUID clientId, UUID sellerId) {
        this.clientId = clientId;
        this.pricePerAction = pricePerAction;
        this.sharesNumber = sharesNumber;
        this.type = OrderType.BUY;
        this.orderId = UUID.randomUUID();
        this.sellerId = sellerId;
    }

    public Order (String data) {
        String[] splitData = data.split(":");

        if (splitData.length >= 5 && splitData.length <= 6) {
            this.type = (splitData[0].equals("BUY")) ? OrderType.BUY : OrderType.SELL;
            this.orderId = UUID.fromString(splitData[1]);
            this.clientId = UUID.fromString(splitData[2]);
            this.sharesNumber = Integer.parseInt(splitData[3]);
            this.pricePerAction = Double.parseDouble(splitData[4]);
            if (this.type == OrderType.BUY)
                this.sellerId = UUID.fromString(splitData[4]);
            else this.sellerId = null;
        }
        else
            throw new IllegalArgumentException("Incorect order string");
    }

    @Override
    public String toString() {
        String typeAsString = (this.type == OrderType.BUY) ? "BUY" : "SELL";
        String orderString = typeAsString + ":" + this.orderId.toString() + ":" + this.clientId.toString() + ":" + this.sharesNumber + ":"  + this.pricePerAction;
        if (this.type == OrderType.BUY)
            orderString = orderString + ":" + this.sellerId;
        return orderString;
    }


    public enum OrderType {
        BUY, SELL;
    }
}

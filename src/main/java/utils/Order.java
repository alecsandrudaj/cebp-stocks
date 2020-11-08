package utils;

import java.util.UUID;

public class Order {
    private int sharesNumber;
    private double pricePerAction;
    private UUID clientId, orderId;
    private OrderType type;

    public Order(int sharesNumber, double pricePerAction, UUID clientId, OrderType type) {
        this.clientId = clientId;
        this.pricePerAction = pricePerAction;
        this.sharesNumber = sharesNumber;
        this.type = type;
        this.orderId = UUID.randomUUID();
    }

    public Order (String data) {
        String[] splitData = data.split(":");

        if (splitData.length == 5) {
            this.type = (splitData[0].equals("BUY")) ? OrderType.BUY : OrderType.SELL;
            this.orderId = UUID.fromString(splitData[1]);
            this.clientId = UUID.fromString(splitData[2]);
            this.sharesNumber = Integer.parseInt(splitData[3]);
            this.pricePerAction = Double.parseDouble(splitData[4]);
        }
        else
            throw new IllegalArgumentException("Incorect order string");
    }

    public double getPricePerAction(){
        return this.pricePerAction;
    }

    public int getSharesNumber() {
        return sharesNumber;
    }

    public void setSharesNumber(int sharesNumber){
        this.sharesNumber = sharesNumber;
    }

    public void setPricePerAction(double pricePerAction){
        this.pricePerAction = pricePerAction;
    }


    @Override
    public String toString() {
        String typeAsString = (this.type == OrderType.BUY) ? "BUY" : "SELL";
        return typeAsString + ":" + this.orderId.toString() + ":" + this.clientId.toString() + ":" + this.sharesNumber + ":"  + this.pricePerAction;
    }



    public enum OrderType {
        BUY, SELL;
    }
}

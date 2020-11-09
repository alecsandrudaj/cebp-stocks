package utils;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;

public class OrderBook {

    private Queue<Order> buyOrders = new ConcurrentLinkedQueue<Order>();
    private Queue<Order> sellOrders = new ConcurrentLinkedQueue<Order>();
    private Queue<Order> history = new ConcurrentLinkedQueue<Order>();

    public Queue<Order> getBuyOrders(){
        return buyOrders;
    }

    public Queue<Order> getSellOrders(){
        return sellOrders;
    }

    public Queue<Order> getHistoryOrders(){
        return history;
    }

    public void putBuyOrderIfAbsent(Order order) {
        synchronized (buyOrders) {
            boolean absent = !buyOrders.contains(order);
            if (absent) {
                buyOrders.add(order);
                System.out.println("Buy order ( " + order.getSharesNumber() + ", " + order.getPricePerAction() + ") was added to active orders.");
            }
        }
    }


    public void putSellOrderIfAbsent(Order order) {
        synchronized (sellOrders) {
            boolean absent = !sellOrders.contains(order);
            if (absent) {
                sellOrders.add(order);
                System.out.println("Sell order ( " + order.getSharesNumber() + ", " + order.getPricePerAction() + ") was added to active orders.");
            }
        }
    }

    public void putHistoryOrder(Order order) {
        synchronized (history) {
            history.add(order);
            System.out.println(order.getType() + " order from client with id " + order.getClientId() + " was added to history.");
        }
    }

    public void removeSellOrder(Order order){
        synchronized (sellOrders){
            if(sellOrders.contains(order)) {
                sellOrders.remove(order);
                System.out.println("Sell order from client with id: " + order.getClientId() + " was removed.");
            }
        }
    }

    public void removeBuyOrder(Order order){
        synchronized (buyOrders) {
            if (buyOrders.contains(order)) {
                buyOrders.remove(order);
                System.out.println("Buy order from client with id: " + order.getClientId() + " was removed.");
            }
        }
    }

    public void modifyOrder(UUID orderId, String orderType, int sharesNumber, double price, UUID clientID, String fieldToModify) {
        Queue<Order> orderListToLookIn;
        if(orderType.equals("SELL"))
            orderListToLookIn=sellOrders;
        else
            orderListToLookIn=buyOrders;

        synchronized (orderListToLookIn){
            for (Order o : orderListToLookIn){
                if((o.getOrderId()).equals(orderId)){
                    if(!(o.getClientId()).equals(clientID))
                        System.out.println("ClientID: " + clientID + "doesn't have permission to modify orderId: " + orderId);
                    else {
                        if(fieldToModify.equals("sharesNumber"))
                            o.setSharesNumber(sharesNumber);
                        else if(fieldToModify.equals("price"))
                            o.setPricePerAction(price);
                    }
                }
            }
        }
    }


    @Override
    public String toString() {
        return  "Buy Orders = " + buyOrders.toString() +
                ", Sell Orders = " + sellOrders.toString() +
                ", History = " + history.toString();
    }
}

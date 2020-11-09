package utils;
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
            else
                this.modifyBuyOrder(order);
        }
    }

    public void modifyBuyOrder(Order order) {
        synchronized (buyOrders) {
            for(Order o : buyOrders)
                if(o.equals(order)){
                    System.out.println("Buy order ( " + o.getSharesNumber() + ", " + o.getPricePerAction() + ") was modified to ( " + order.getSharesNumber() + ", " + order.getPricePerAction() + ")." );
                    o.setPricePerAction(order.getPricePerAction());
                    o.setSharesNumber(order.getSharesNumber());
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
            else
                modifySellOrder(order);
        }
    }

    public void modifySellOrder(Order order) {
        synchronized (sellOrders) {
            for(Order o : sellOrders)
                if(o.equals(order)){
                    System.out.println("Sell order ( " + o.getSharesNumber() + ", " + o.getPricePerAction() + ") was modified to ( " + order.getSharesNumber() + ", " + order.getPricePerAction() + ")." );
                    o.setPricePerAction(order.getPricePerAction());
                    o.setSharesNumber(order.getSharesNumber());
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

}

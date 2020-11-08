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
            if (absent)
                buyOrders.add(order);
            else
                this.modifyBuyOrder(order);
        }
    }

    public void modifyBuyOrder(Order order) {
        synchronized (buyOrders) {
            for(Order o : buyOrders)
                if(o.equals(order)){
                    o.setPricePerAction(order.getPricePerAction());
                    o.setSharesNumber(order.getSharesNumber());
                }
        }
    }

    public void putSellOrderIfAbsent(Order order) {
        synchronized (sellOrders) {
            boolean absent = !sellOrders.contains(order);
            if (absent)
                sellOrders.add(order);
            else
                modifySellOrder(order);
        }
    }

    public void modifySellOrder(Order order) {
        synchronized (sellOrders) {
            for(Order o : sellOrders)
                if(o.equals(order)){
                    o.setPricePerAction(order.getPricePerAction());
                    o.setSharesNumber(order.getSharesNumber());
                }
        }
    }

    public void putHistoryOrderIfAbsent(Order order) {
        synchronized (history) {
            boolean absent = !history.contains(order);
            if (absent)
                history.add(order);
        }
    }

    public void removeSellOrder(Order order){
        synchronized (sellOrders){
            if(sellOrders.contains(order))
                sellOrders.remove(order);
        }
    }

    public void removeBuyOrder(Order order){
        synchronized (buyOrders) {
            if (buyOrders.contains(order))
                buyOrders.remove(order);
        }
    }

}

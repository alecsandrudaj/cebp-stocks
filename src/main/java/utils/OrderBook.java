package utils;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {

    private Queue<Order> orders = new ConcurrentLinkedQueue<Order>();
    private TransactionHistory history = new TransactionHistory();
    private Lock lock1 = new ReentrantLock();


    private void modifyOrder(Order orderToModify, int shares, double price) {
        orderToModify.setSharesNumber(shares);
        orderToModify.setPricePerAction(price);
    }

    private void makeTransaction(Order incomingOrder, Order matchOrder) {
        int incomingShares = incomingOrder.getSharesNumber();
        int matchShares = matchOrder.getSharesNumber();

        if (incomingShares == matchShares) {
            orders.remove(matchOrder);
            System.out.println(incomingOrder.toString() + "matched " + matchOrder.toString() + '\n');
            history.saveTransaction(incomingOrder, matchOrder);
        }
        else if (incomingShares < matchShares) {
            
        }
    }
    public boolean checkMatch (Order incomingOrder) {
        lock1.lock();
        boolean isMatch = false;
        Order match = null;
        for (Order o : orders) {
            if (incomingOrder.getType() == Order.OrderType.BUY && o.getType() == Order.OrderType.SELL && incomingOrder.getPricePerAction() >= o.getPricePerAction()) {
                isMatch = true;
                match = o;
                break;
            }
            else if (incomingOrder.getType() == Order.OrderType.SELL && o.getType() == Order.OrderType.BUY && incomingOrder.getPricePerAction() <= o.getPricePerAction()) {
                isMatch = true;
                match = o;
                break;
            }

        if (isMatch) {


        }

        }
    }

}

package utils;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OrderBook {

    private Queue<Order> orders = new ConcurrentLinkedQueue<Order>();
    private TransactionHistory history = new TransactionHistory();
    private Lock lock1 = new ReentrantLock();

    public String getHistory() {
        return history.toString();
    }

    private String getActiveOrders() {
        StringBuilder result = new StringBuilder();
        for (Order o : orders) {
            result.append(o.toString()).append('\n');
        }
        return result.toString();
    }
    private void modifyOrder(Order orderToModify, int shares, double price) {
        orderToModify.setSharesNumber(shares);
        orderToModify.setPricePerAction(price);
    }

    private void makeTransaction(Order incomingOrder, Order matchOrder) {
        int incomingShares = incomingOrder.getSharesNumber();
        int matchShares = matchOrder.getSharesNumber();
        history.saveTransaction(incomingOrder, matchOrder);
        if (incomingShares == matchShares) {
            System.out.println(incomingOrder.toString() + " matched " + matchOrder.toString() + '\n');
            orders.remove(matchOrder);
        }
        else if (incomingShares < matchShares) {
            System.out.println(incomingOrder.toString() + " matched partially " + matchOrder.toString() + '\n');
            modifyOrder(matchOrder, matchShares-incomingShares , matchOrder.getPricePerAction());
            System.out.println("Modified order: " + matchOrder.toString() + '\n');

        }
        else {
            System.out.println(incomingOrder.toString() + " matched partially " + matchOrder.toString() + '\n');;
            modifyOrder(incomingOrder, incomingShares-matchShares, incomingOrder.getPricePerAction());
            orders.add(incomingOrder);
            orders.remove(matchOrder);
            System.out.println("New partial order added: " + incomingOrder.toString() + '\n');
        }
    }

    public void checkMatchorAdd (Order incomingOrder) {
        lock1.lock();
        try {
            boolean isMatch = false;
            Order match = null;
            for (Order o : orders) {
                if (incomingOrder.getType() == Order.OrderType.BUY && o.getType() == Order.OrderType.SELL && incomingOrder.getPricePerAction() >= o.getPricePerAction()) {
                    isMatch = true;
                    match = o;
                    break;
                } else if (incomingOrder.getType() == Order.OrderType.SELL && o.getType() == Order.OrderType.BUY && incomingOrder.getPricePerAction() <= o.getPricePerAction()) {
                    isMatch = true;
                    match = o;
                    break;
                }
            }

            if (isMatch) {
                makeTransaction(incomingOrder, match);
            } else {
                orders.add(incomingOrder);
                System.out.println("No match, adding order: " + incomingOrder.toString() + '\n');
            }
        }
        finally {
            System.out.println("Active orders: \n" + getActiveOrders());
            lock1.unlock();
        }
    }

}

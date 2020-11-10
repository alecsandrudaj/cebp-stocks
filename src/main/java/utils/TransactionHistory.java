package utils;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TransactionHistory {
    private ArrayList<Transaction> history = new ArrayList();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override
    public String toString() {
        lock.readLock().lock();
            try {
                StringBuilder result = new StringBuilder();
                for (Transaction t : history) {
                    result.append(t.toString()).append('\n');
                }
                return result.toString();
            }
            finally {
                lock.readLock().unlock();
            }
    }


    public void saveTransaction(Order incomingOrder, Order match) {
        lock.writeLock().lock();
        try {
            UUID sellerId = (incomingOrder.getType() == Order.OrderType.SELL) ? incomingOrder.getClientId() : match.getClientId();
            UUID buyerId = (incomingOrder.getType() == Order.OrderType.BUY) ? incomingOrder.getClientId() : match.getClientId();
            int shares = Math.min(incomingOrder.getSharesNumber(), match.getSharesNumber());
            double price = Math.max(incomingOrder.getPricePerAction(), match.getPricePerAction());
            Transaction t = new Transaction(sellerId, buyerId, shares, price);
            history.add(t);
            System.out.println("Added transaction " + t + " to history\n");
        }
        finally {
            lock.writeLock().unlock();
        }
    }
}

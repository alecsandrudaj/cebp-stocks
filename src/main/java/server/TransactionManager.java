package server;

import utils.Order;
import utils.OrderBook;
import java.util.Queue;

public class TransactionManager {
    private OrderBook orderManager = new OrderBook();

    public void handleOrder(Order e) {
        orderManager.checkMatchorAdd(e);
    }

    public String getTransactionHistory() {
        return orderManager.getHistory();
    }
}

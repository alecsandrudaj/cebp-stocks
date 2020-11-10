package simulation;

import client.Client;
import utils.Order;

public class Seller1 implements Runnable {
    @Override
    public void run() {
        Client client = new Client(819);
        client.connect();
        client.sendInitMessage();

        Order sell1 = new Order(1000, 2.5, client.getClientId(), Order.OrderType.SELL);
        client.sendOrder(sell1);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order sell2 = new Order(200, 10, client.getClientId(), Order.OrderType.SELL);
        client.sendOrder(sell2);
    }
}

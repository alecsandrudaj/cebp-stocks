package simulation;

import client.Client;
import utils.Order;

public class Buyer2 implements Runnable {
    @Override
    public void run() {
        Client client = new Client(819);
        client.connect();

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        client.sendInitMessage();

        Order buy1 = new Order(800, 2.5, client.getClientId(), Order.OrderType.BUY);
        client.sendOrder(buy1);

        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order buy2 = new Order(200, 10, client.getClientId(), Order.OrderType.BUY);
        client.sendOrder(buy2);
    }
}

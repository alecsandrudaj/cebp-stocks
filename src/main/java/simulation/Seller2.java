package simulation;

import client.Client;
import utils.Order;

public class Seller2 implements Runnable {
    @Override
    public void run() {
        Client client = new Client(819);
        client.connect();
        client.sendInitMessage();

        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Order sell1 = new Order(1000, 1, client.getClientId(), Order.OrderType.SELL);
        client.sendOrder(sell1);

        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        client.getHistory();
    }
}

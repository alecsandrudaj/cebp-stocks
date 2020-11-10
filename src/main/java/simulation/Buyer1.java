package simulation;

import client.Client;
import utils.Order;

public class Buyer1 implements Runnable {
    @Override
    public void run() {
        Client client = new Client(819);
        client.connect();
        client.sendInitMessage();

        Order buy1 = new Order(1500, 1.2, client.getClientId(), Order.OrderType.BUY);
        client.sendOrder(buy1);

    }
}

package client;

import utils.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Client {

    private UUID clientId;
    private int port;
    private BufferedReader inputConn;
    private PrintWriter outputConn;
    private  Socket connection;

    public UUID getClientId() {
        return clientId;
    }

    public Client (int port) {
        this.port = port;
        this.clientId = UUID.randomUUID();
    }

    public boolean connect() {
        try {
            connection = new Socket("localhost", this.port);
            this.inputConn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.outputConn = new PrintWriter(connection.getOutputStream(), true);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean closeConnection() {
        try {
            this.connection.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public void sendInitMessage() {
        this.outputConn.println("INIT:" + this.clientId);
    }

    public void sendOrder(Order o) {
        this.outputConn.println(o.toString());
    }

    public void modfiyOrderShares(UUID orderID, int newShares) {
        this.outputConn.println("MODIFY:"+ orderID + ":" + "shares:" + newShares);
    }

    public void modfiyOrderPrice(UUID orderID, double newPrice) {
        this.outputConn.println("MODIFY:"+ orderID + ":" + "price:" + newPrice);
    }

    public void getHistory() {
        this.outputConn.println("HISTORY:");
        System.out.println("History recieved for client: " + clientId);

        try {
            int n = Integer.parseInt(this.inputConn.readLine());
            System.out.println(n + " transactions");
            while (n>0) {
                System.out.println(this.inputConn.readLine());
                n--;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Done history");
    }

    public static void main(String[] args) {
        System.out.println("**Start stock exchange**");

        //Seller 1
        Client seller_1 = new Client(819);
        seller_1.connect();
        seller_1.sendInitMessage();

        Order sell1 = new Order(1000, 2, seller_1.clientId, Order.OrderType.SELL);
        seller_1.sendOrder(sell1);

        Client buyer_1 = new Client(819);
        buyer_1.connect();
        buyer_1.sendInitMessage();

        Order buy1 = new Order(800, 2, buyer_1.clientId, Order.OrderType.BUY);
        buyer_1.sendOrder(buy1);

        Client buyer_2 = new Client(819);
        buyer_2.connect();
        buyer_2.sendInitMessage();

        Order sell2 = new Order(200, 5, seller_1.clientId, Order.OrderType.SELL);
        seller_1.sendOrder(sell2);

        Order buy2 = new Order(500, 5, buyer_2.clientId, Order.OrderType.BUY);
        buyer_2.sendOrder(buy2);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        buyer_2.getHistory();
    }
}

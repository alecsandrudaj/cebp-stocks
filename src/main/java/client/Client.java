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

    public static void main(String[] args) {
        System.out.println("**Start stock exchange**");

        //Seller 1
        Client seller_1 = new Client(789);
        seller_1.connect();
        seller_1.sendInitMessage();

        //Seller 2
//        Client seller_2 = new Client(789);
//        seller_2.connect();
//        seller_2.sendInitMessage();

        //Buyer 1
        Client buyer_1 = new Client(789);
        buyer_1.connect();
        buyer_1.sendInitMessage();

        //Buyer 2
        Client buyer_2 = new Client(789);
        buyer_2.connect();
        buyer_2.sendInitMessage();

        Order sellOrder_1 = new Order(1000, 25, seller_1.clientId, Order.OrderType.SELL);
        seller_1.sendOrder(sellOrder_1);
//        sellOrder_1.setPricePerAction(24);
//        seller_1.sendOrder(sellOrder_1);

        Order sellOrder_2 = new Order(80, 24, seller_1.clientId, Order.OrderType.SELL);
        seller_1.sendOrder(sellOrder_2);

        Order buyOrder_1 = new Order(1000, 24, buyer_1.clientId, Order.OrderType.BUY);
        buyer_1.sendOrder(buyOrder_1);

        Order buyOrder_2 = new Order(900, 25, buyer_2.clientId, Order.OrderType.BUY);
        buyer_2.sendOrder(buyOrder_2);

    }
}

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
        System.out.println("Start stock exchange ");
        Client client = new Client(789);
        client.connect();

        Client cl2 = new Client(789);
        cl2.connect();
        cl2.sendInitMessage();

        client.sendInitMessage();
        client.sendInitMessage();

        Order or1 = new Order(1000, 24.353, client.clientId, Order.OrderType.SELL);
        client.sendOrder(or1);
        or1.setPricePerAction(25);
        client.sendOrder(or1);

        Order or2 = new Order(500, 25.353, client.clientId, Order.OrderType.BUY);
        cl2.sendOrder(or2);
    }
}

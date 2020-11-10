package server;

import utils.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class Connection implements Runnable {
    private Socket connection;
    private BufferedReader inputConn;
    private PrintWriter outputConn;
    private UUID clientID;
    private TransactionManager transactionManager;

    public Connection(Socket clientSocket, TransactionManager transactionManager) {
        this.connection = clientSocket;
        this.transactionManager = transactionManager;
    }
    @Override
    public void run() {
        try {
            this.inputConn = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            this.outputConn = new PrintWriter(connection.getOutputStream(), true);
            String data;
            while ((data = this.inputConn.readLine()) != null) {
                String[] splitInput = data.split(":");
                String connType = splitInput[0];
                switch (connType) {
                    case "INIT":
                        this.clientID = UUID.fromString(splitInput[1]);
                        System.out.println("Received init from client" + clientID + '\n');
                        break;
                    case "BUY":
                        System.out.println("Recieved buy order from client" + clientID + '\n');
                        transactionManager.handleOrder(new Order(data));
                        break;
                    case "SELL":
                        System.out.println("Received sell order from client" + clientID);
                        transactionManager.handleOrder(new Order(data));
                        break;
                    case "HISTORY":
                        System.out.println("Recieved history request from client" + clientID);
                        String result = transactionManager.getTransactionHistory();
                        result.lines().forEach(outputConn::println);
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read messages from client:" + e.getMessage() + '\n');
        }



    }
}

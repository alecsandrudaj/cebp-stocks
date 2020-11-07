package server;

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

    public Connection(Socket clientSocket) {
        this.connection = clientSocket;
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
                        System.out.println("Recieved init from client" + clientID + '\n');
                        // register client
                        break;
                    case "BUY":
                        System.out.println("Recieved buy order from client" + clientID + '\n');
                        System.out.println(data);
                        // buy order
                        break;
                    case "SELL":
                        System.out.println("Recieved sell order from client\n" + clientID);
                        System.out.println(data);
                        // sell order:
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Can't read messages from client:" + e.getMessage() + '\n');
        }



    }
}
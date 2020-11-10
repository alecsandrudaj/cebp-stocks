package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
            Server server = new Server();
            server.run(819);
    }

    public void run(int port) {
        Socket clientSocket = null;
        TransactionManager transactionManager = new TransactionManager();
        try {
            this.serverSocket = new ServerSocket(port);
            while (true) {
                clientSocket = this.serverSocket.accept();
                new Thread(new Connection(clientSocket, transactionManager)).start();
            }
        }
        catch (IOException e) {
            System.out.println("Server doesn't work correctly: " + e.getMessage());
        }


    }
}

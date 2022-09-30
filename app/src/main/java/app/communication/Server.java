package app.communication;

import java.io.IOException;
import java.net.ServerSocket;


public class Server {
    private ServerSocket serverSocket;

    public static void main(String[] args) {
        Server server = new Server();
        server.listenSocket();
    }

    public void listenSocket() {
        try {
            serverSocket = new ServerSocket(4321);
        } catch (IOException e) {
            System.out.println("Could not listen on port 4321");
            System.exit(-1);
        }
    }
}

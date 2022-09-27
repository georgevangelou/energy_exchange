package com.investigations;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class PeerNode {
    private int port;
    private ArrayList<PeerNode> contacts;
    private PeerNode preNode;
    private PeerNode postNode;
    ServerSocket server;
    private String directoryLocation = "";

    public PeerNode(int port) {
        this.directoryLocation = port + "";
        new Thread(new Runnable() {
            public void run() {
                startClientServer();
            }
        }).start();
    }

    public int getPort() {
        return port;
    }


    public void sendRequest(String host, int port) throws UnknownHostException, IOException {
        Socket socket = new Socket(host, port);//machine name, port number
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
//        out.println(fileName);
        System.out.println("Sending to port: " + port);
//        sendRequest("localhost", arrayOfNodes.get(0).getPort())

        out.close();
        socket.close();
    }

    private void startClientServer() {
        try {
            // Establish the listen socket.
            this.server = new ServerSocket(this.port);
            this.port = server.getLocalPort();
            System.out.println("listening on port " + this.port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void acceptRequests() {
        try {
            while (true) {
                // Listen for a TCP connection request.
                Socket connection = this.server.accept();

                // Construct an object to process the HTTP request message.
                HttpRequestHandler request = new HttpRequestHandler(connection);

                // Create a new thread to process the request.
                Thread thread = new Thread(request);

                // Start the thread.
                thread.start();
                System.out.println("Thread started for " + this.port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

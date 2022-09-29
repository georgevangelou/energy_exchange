package app.investigations;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PeerNode {
    private final Logger LOGGER = LoggerFactory.getLogger(PeerNode.class.getName());
    private int port;
    private ArrayList<PeerNode> contacts;
    private PeerNode preNode;
    private PeerNode postNode;
    private List<String> receivedMessages = new ArrayList<>();
    private ServerSocket server;
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


    public void sendRequest(String host, int targetPort, String content) throws IOException {
        Socket socket = new Socket(host, targetPort);//machine name, port number
        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
        LOGGER.debug("This is my port: " + this.port);
        LOGGER.debug("Sending to port: " + targetPort);

        LOGGER.debug("My content is: " + content);
        out.write(content);
//        sendRequest("localhost", arrayOfNodes.get(0).getPort())

        out.close();
        socket.close();
    }

    private void startClientServer() {
        try {
            // Establish the listen socket.
            this.server = new ServerSocket(this.port);
            this.port = server.getLocalPort();
            LOGGER.debug("listening on port " + this.port);
            acceptRequests();
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
                String content = new String(request.socket.getInputStream().readAllBytes());
                JsonObject json = new JsonParser().parse(content).getAsJsonObject();
                receivedMessages.add(json.toString());

                LOGGER.debug("\nThis is my port: " + connection.getLocalPort());
                LOGGER.debug("Received from port: " + this.port);

//                System.out.println("I received type: " + json.get("type"));
//                System.out.println("I received index: " + json.get("index"));
                // Create a new thread to process the request.
//                Thread thread = new Thread(request);

                // Start the thread.
//                thread.start();
//                System.out.println("Thread started for " + this.port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<String> getReceivedMessages() {
        return receivedMessages;
    }
}

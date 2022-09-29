package app.investigations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public abstract class PeerNode {
    private final Logger LOGGER = LoggerFactory.getLogger(PeerNode.class.getName());
    private int port = 0;
    private List<PeerNode> contacts = new ArrayList<>();
    protected List<String> receivedResponses = new ArrayList<>();
    protected ServerSocket server;


    public PeerNode(int port) {
        new Thread(new Runnable() {
            public void run() {
                startClientServer(port);
            }
        }).start();
    }

    public int getPort() {
        return port;
    }


    public void sendMessage(String host, int targetPort, String content) throws IOException {
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

    private void startClientServer(int port) {
        try {
            // Establish the listen socket.
            this.server = new ServerSocket(port);
            this.port = server.getLocalPort();
            LOGGER.debug("listening on port " + this.port);
            acceptRequests();
        } catch (Exception e) {
            LOGGER.error("Exception thrown: ", e);
        }
    }

    protected abstract void acceptRequests();

    public List<String> getReceivedResponses() {
        return receivedResponses;
    }
}

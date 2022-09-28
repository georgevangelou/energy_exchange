package app.investigations;

import java.io.IOException;
import java.util.ArrayList;

public class MasterClientServer {
    private final static int port = 0;

    public static void main(String[] args) throws IOException, InterruptedException {
        int count = 2;
        ArrayList<PeerNode> arrayOfNodes = createNodes(count);
        Thread.sleep(500);
        System.out.println(arrayOfNodes.get(0).getPort());
        System.out.println(arrayOfNodes.get(1).getPort());

        int i = 0;
        while (true) {
            arrayOfNodes.get(0).sendRequest("localhost", arrayOfNodes.get(1).getPort(), Integer.toString(i++));
            Thread.sleep(500);
            arrayOfNodes.get(1).sendRequest("localhost", arrayOfNodes.get(0).getPort(), Integer.toString(i++));
            Thread.sleep(500);
        }
    }

    public static ArrayList<PeerNode> createNodes(int count) {
        System.out.println("Creating a network of " + count + " nodes...");
        ArrayList<PeerNode> arrayOfNodes = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            arrayOfNodes.add(new PeerNode(port)); //providing 0, will take any free node
        }
        return arrayOfNodes;
    }
}




package com.investigations;

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
        while (true) {
            arrayOfNodes.get(0).sendRequest("localhost", arrayOfNodes.get(1).getPort());
            Thread.sleep(100);
            arrayOfNodes.get(1).sendRequest("localhost", arrayOfNodes.get(0).getPort());
        }
    }

    public static ArrayList<PeerNode> createNodes(int count) {
        System.out.println("Creating a network of " + count + " nodes...");
        ArrayList<PeerNode> arrayOfNodes = new ArrayList<PeerNode>();

        for (int i = 1; i <= count; i++) {
            arrayOfNodes.add(new PeerNode(port)); //providing 0, will take any free node
        }
        return arrayOfNodes;
    }
}




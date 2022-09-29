package app.investigations;

import java.util.ArrayList;

public class MasterPeerNode {
    private final static int port = 0;

    public static ArrayList<PeerNode> createNodes(int count) {
        System.out.println("Creating a network of " + count + " nodes...");
        ArrayList<PeerNode> arrayOfNodes = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            arrayOfNodes.add(new PeerNode(port)); //providing port=0, will take any free port
        }
        return arrayOfNodes;
    }
}




package app.investigations;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;

public class MasterClientServer {
    private final static int port = 0;

    public static ArrayList<PeerNode> createNodes(int count) {
        System.out.println("Creating a network of " + count + " nodes...");
        ArrayList<PeerNode> arrayOfNodes = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            arrayOfNodes.add(new PeerNode(port)); //providing 0, will take any free node
        }
        return arrayOfNodes;
    }
}




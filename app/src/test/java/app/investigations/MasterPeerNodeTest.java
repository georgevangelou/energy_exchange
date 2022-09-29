package app.investigations;

import com.google.gson.JsonObject;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static app.investigations.MasterPeerNode.createNodes;

public class MasterPeerNodeTest {

    @Test
    public void jsonSerializeDeserialize() throws InterruptedException, IOException {
        int count = 2;
        ArrayList<PeerNode> arrayOfNodes = createNodes(count);
        Thread.sleep(500);
        System.out.println(arrayOfNodes.get(0).getPort());
        System.out.println(arrayOfNodes.get(1).getPort());

        JsonObject json;
        for (int i = 0; i < 10; i++) {
            json = new JsonObject();
            json.addProperty("type", "r01");
            json.addProperty("index", i++);
            arrayOfNodes.get(0).sendRequest("localhost", arrayOfNodes.get(1).getPort(), json);
            Thread.sleep(500);

            json = new JsonObject();
            json.addProperty("type", "r01");
            json.addProperty("index", i++);
            arrayOfNodes.get(1).sendRequest("localhost", arrayOfNodes.get(0).getPort(), json);
            Thread.sleep(500);
        }
    }
}
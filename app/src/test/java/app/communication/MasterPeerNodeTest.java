package app.communication;

import com.google.gson.JsonObject;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

import static app.communication.MasterPeerNode.createNodes;

public class MasterPeerNodeTest {
    private final Logger LOGGER = LoggerFactory.getLogger(MasterPeerNodeTest.class.getName());


    @Test
    public void sendReceiveJson() throws InterruptedException, IOException {
        int count = 2;
        ArrayList<PeerNode> arrayOfNodes = createNodes(count);
        Thread.sleep(500);
        LOGGER.info(Integer.toString(arrayOfNodes.get(0).getPort()));
        LOGGER.info(Integer.toString(arrayOfNodes.get(1).getPort()));

        JsonObject json;
        for (int i = 0; i < 10; i++) {
            json = new JsonObject();
            json.addProperty("type", "r01");
            json.addProperty("index", i++);
            arrayOfNodes.get(0).sendMessage("localhost", arrayOfNodes.get(1).getPort(), json.toString());
            Thread.sleep(500);

            json = new JsonObject();
            json.addProperty("type", "r01");
            json.addProperty("index", i++);
            arrayOfNodes.get(1).sendMessage("localhost", arrayOfNodes.get(0).getPort(), json.toString());
            Thread.sleep(500);
        }
    }
}
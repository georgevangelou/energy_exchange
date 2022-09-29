package app.agents;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ProducerTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void twoProducersExchangeStatus() {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        int portP1 = p1.getPort();
        int portP2 = p2.getPort();

        p1.getPortsOfOtherPeers().add(portP2);
        p2.getPortsOfOtherPeers().add(portP1);

        p1.updateBlockChain();
        p2.updateBlockChain();
    }


    @After
    public void tearDown() throws Exception {
    }
}
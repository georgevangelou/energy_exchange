package app.agents;

import app.properties.BidTransactionBlock;
import app.properties.Block;
import app.properties.Blockchain;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProducerTest {
    private final Logger LOGGER = LoggerFactory.getLogger(ProducerTest.class.getName());


    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void twoProducersCheckTheirStatuses() {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        int portP1 = p1.getPort();
        int portP2 = p2.getPort();

        p1.getPortsOfOtherPeers().add(portP2);
        p2.getPortsOfOtherPeers().add(portP1);

        p1.updateBlockChain();
        p2.updateBlockChain();
    }


    @Test
    public void oneProducerUpdatesStatusFromAnother() {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        int portP1 = p1.getPort();
        int portP2 = p2.getPort();

        p1.getPortsOfOtherPeers().add(portP2);
        p2.getPortsOfOtherPeers().add(portP1);

        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());

        Blockchain chain = new Blockchain();

        Block b1 = new BidTransactionBlock("0", 5, 0, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        Block b2 = new BidTransactionBlock(b1.getHash(), 5, 0, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        chain.add(b1);
        chain.add(b2);

        p1.replaceBlockchain(chain);
        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());

        p1.updateBlockChain();
        p2.updateBlockChain();

        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());
    }


    @After
    public void tearDown() throws Exception {
    }
}
package app.agents;

import app.properties.BidTransactionBlock;
import app.properties.Block;
import app.properties.Blockchain;
import org.junit.After;
import org.junit.Assert;
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
    public void twoProducersEnterTheNetwork() {
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
    public void oneProducerUpdatesStatusAndBlockchainFromAnother() {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        int portP1 = p1.getPort();
        int portP2 = p2.getPort();

        p1.getPortsOfOtherPeers().add(portP2);
        p2.getPortsOfOtherPeers().add(portP1);

        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());

        Blockchain chain = new Blockchain();

        Block b0 = new BidTransactionBlock("0", 5, 0, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        Block b1 = new BidTransactionBlock(b0.getHash(), 5, 1, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        chain.add(b0);
        chain.add(b1);

        p1.replaceBlockchain(chain);
        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());

        p1.updateBlockChain();
        p2.updateBlockChain();

        LOGGER.info("Producer 1 - Hash difficulty: " + p1.getStatus().getTotalHashDifficulty());
        LOGGER.info("Producer 2 - Hash difficulty: " + p2.getStatus().getTotalHashDifficulty());
    }


    @Test
    public void oneProducerSendsNewBlockAndAnotherProducerInsertsItInItsBlockchain() throws InterruptedException {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        p1.getPortsOfOtherPeers().add(p2.getPort());
        p2.getPortsOfOtherPeers().add(p1.getPort());

        Blockchain chain1 = new Blockchain();
        Blockchain chain2 = new Blockchain();

        Block b0 = new BidTransactionBlock("0", 5, 0, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        chain1.add(b0);
        chain2.add(b0);

        p1.replaceBlockchain(chain1);
        p2.replaceBlockchain(chain2);
        Thread.sleep(1_000);
        p1.generateAndBroadcastBlock();
        Thread.sleep(1_000);

        LOGGER.info("P1 blockchain: " + p1.getBlockchain().size());
        LOGGER.info("P2 blockchain: " + p2.getBlockchain().size());
        Assert.assertEquals( p1.getBlockchain().size(),  p2.getBlockchain().size());
    }


    @Test
    public void oneProducerSendsNewBlockAndAnotherProducerNeedsFullBlockchainUpdate() throws InterruptedException {
        Producer p1 = new Producer(0);
        Producer p2 = new Producer(0);

        p1.getPortsOfOtherPeers().add(p2.getPort());
        p2.getPortsOfOtherPeers().add(p1.getPort());

        Blockchain chain1 = new Blockchain();
        Blockchain chain2 = new Blockchain();

        Block b0 = new BidTransactionBlock("0", 5, 0, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        Block b1 = new BidTransactionBlock(b0.getHash(), 5, 1, "0",
                "0", "0", 1, 2,
                3, new byte[0]);

        chain1.add(b0);
        chain2.add(b0);
        chain1.add(b1);

        p1.replaceBlockchain(chain1);
        p2.replaceBlockchain(chain2);
        Thread.sleep(1_000);
        p1.generateAndBroadcastBlock();
        Thread.sleep(1_000);

        LOGGER.info("P1 blockchain: " + p1.getBlockchain().size());
        LOGGER.info("P2 blockchain: " + p2.getBlockchain().size());
        Assert.assertEquals( p1.getBlockchain().size(),  p2.getBlockchain().size());
    }



    @After
    public void tearDown() throws Exception {
    }
}
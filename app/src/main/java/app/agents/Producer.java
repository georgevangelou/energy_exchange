package app.agents;

import app.investigations.Message;
import app.investigations.PeerNode;
import app.properties.*;
import app.utilities.UUIDGenerator;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * Functionality:
 * o   Generate expected energy production
 * o   Generate energy unit price required to make profit
 * o   Update stored blockchain (i.e., replace chain)
 * o   Submit energy offer to smart contract
 */
public class Producer extends PeerNode {
    private final Logger LOGGER = LoggerFactory.getLogger(PeerNode.class.getName());
    private final long TIMEOUT = 10_000;
    private List<Integer> portsOfOtherPeers = new ArrayList<>();//TODO: Get somehow
    private Random random = new Random();

    private RecentActivity recentActivity = new RecentActivity();
    private Blockchain blockchain = new Blockchain();
    private Wallet wallet = new Wallet(); // Let's have them, right for now
    private Battery battery = new Battery(random.nextInt(), random.nextInt());
    private Status status;
    private Offer offer;
    private Activity activity;


    public Producer(int port) {
        super(port);

        blockchain.add(new BidTransactionBlock(
                "0",
                0,
                0,
                "0",
                "0",
                "0",
                0,
                0,
                0,
                new byte[0])
        );

        this.status = new Status(0);
        updateBlockChain();
    }


    private int generateExpectedEnergyProduction() {
        int low = 5;
        int high = 10;
        int expectedEnergyProduction = random.nextInt(high - low) + low;
        return expectedEnergyProduction;
    }


    private int generateEnergyUnitPrice() {
        int low = 10;
        int high = 100;
        int energyUnitPrice = random.nextInt(high - low) + low;
        return energyUnitPrice;
    }

    private void updateBlockChain() {
        for (int peerPort : portsOfOtherPeers) {
            updateBlockChainFromPeer(peerPort);
        }
    }

    private void updateBlockChainFromPeer(int peerPort) {
        Status statusOfOtherPeer = askOtherPeerForStatus(peerPort);
        if (statusOfOtherPeer.getTotalHashDifficulty() > this.status.getTotalHashDifficulty()) {
            Blockchain otherPeerBlockchain = askOtherPeerForBlockchain(peerPort);
            if (otherPeerBlockchain.isValid()) {
                this.blockchain = otherPeerBlockchain;
                this.status = new Status(this.blockchain.getLast().getHashDifficulty());
            }
        }
    }


    private Status askOtherPeerForStatus(int portOfPeerToAsk) {
        try {
            String requestId = UUIDGenerator.generateUUID();
            Message message = new Message(requestId, "", this.getPort());
            sendRequest("localhost", portOfPeerToAsk, message.toJson());
            Optional<Message> optionalMessage = pollForResponse(requestId);
            if (optionalMessage.isPresent()) {
                Message parsedMessage = optionalMessage.get();
                Status statusReceived = new Gson().fromJson(parsedMessage.getJsonData(), Status.class);
                return statusReceived;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    private Optional<Message> pollForResponse(String requestId) throws InterruptedException {
        Optional<Message> optReceivedMessage = Optional.empty();
        long start = System.currentTimeMillis();

        while ((optReceivedMessage = getMatchingResponse(requestId)).isEmpty()) {
            if (System.currentTimeMillis() - start > TIMEOUT) {
                LOGGER.warn("Did not receive response after ms: " + TIMEOUT);
                break;
            }
            Thread.sleep(300);
        }
        return optReceivedMessage;
    }

    private Optional<Message> getMatchingResponse(String requestId) {
        Gson gson = new Gson();
        List<String> receivedMessages = getReceivedMessages();
        for (String receivedMessage : receivedMessages) {
            Message b = gson.fromJson(receivedMessage, Message.class);
            if (b.getId().equals(requestId)) {
                return Optional.of(b);
            }
        }
        return Optional.empty();
    }

    private Blockchain askOtherPeerForBlockchain(int portOfPeerToAsk) {
        //TODO: ask peer for blockchain
        //TODO: Deserialize it
        return null;
    }


    private void submitEnergyOfferToSmartContract() {
        int expectedEnergyProduction = generateExpectedEnergyProduction();
        int energyUnitPrice = generateEnergyUnitPrice();
        // TODO: pack and send
    }


    private void processIncomingWriteBlockMessage(String peerAddress, int peerPort, String writeBlockMessage) {
        // TODO: get block from message
        //   Message message = new Gson().fromJson(writeBlockMessage, Message.class);
        //   Block block = message.getBlock();
        Block block = new Gson().fromJson(writeBlockMessage, Block.class);
        if (block.getIndex() != this.blockchain.getLast().getIndex() + 1) {
            updateBlockChainFromPeer(peerPort);
        } else {
            if (block.isValid()) {
                this.blockchain.add(block);
                this.status.setTotalHashDifficulty(
                        this.status.getTotalHashDifficulty() +
                                block.getHashDifficulty()
                );
                this.recentActivity.add(new Activity(block.getHash(), peerAddress, Activity.DIRECTION.FROM));

            }
        }
    }

    public Status getStatus() {
        return status;
    }
}

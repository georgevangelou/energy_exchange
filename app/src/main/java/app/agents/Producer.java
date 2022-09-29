package app.agents;

import app.investigations.HttpRequestHandler;
import app.investigations.PeerNode;
import app.messages.Message;
import app.messages.MessageType;
import app.properties.*;
import app.utilities.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Socket;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
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
//
//        blockchain.add(new BidTransactionBlock(
//                "0",
//                0,
//                0,
//                "0",
//                "0",
//                "0",
//                0,
//                0,
//                0,
//                new byte[0])
//        );

        this.status = new Status(0);
        this.updateBlockChain();
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


    public void updateBlockChain() {
        for (int peerPort : portsOfOtherPeers) {
            updateBlockChainFromPeer(peerPort);
        }
    }


    private void updateBlockChainFromPeer(int peerPort) {
        Status statusOfOtherPeer = askOtherPeerForStatus(peerPort);
        if (statusOfOtherPeer == null) {
            return;
        }
        if (statusOfOtherPeer.getTotalHashDifficulty() > this.status.getTotalHashDifficulty()) {
            Blockchain otherPeerBlockchain = askOtherPeerForBlockchain(peerPort);
            if (otherPeerBlockchain != null && otherPeerBlockchain.isValid()) {
                replaceBlockchain(otherPeerBlockchain);
            }
        }
    }


    private Status askOtherPeerForStatus(int portOfPeerToAsk) {
        try {
            String requestId = UUIDGenerator.generateUUID();
            Message message = new Message(requestId, MessageType.STATUS, "", this.getPort());
            sendMessage("localhost", portOfPeerToAsk, message.toJson());
            Optional<Message> optionalMessage = pollForResponse(requestId);
            if (optionalMessage.isPresent()) {
                Message parsedMessage = optionalMessage.get();
                Status statusReceived = new Gson().fromJson(parsedMessage.getJsonData(), Status.class);
                return statusReceived;
            }
        } catch (Exception e) {
            LOGGER.error("Exception thrown: ", e);
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
        try {
            String requestId = UUIDGenerator.generateUUID();
            Message message = new Message(requestId, MessageType.BLOCKCHAIN, "", this.getPort());
            sendMessage("localhost", portOfPeerToAsk, message.toJson());
            Optional<Message> optionalMessage = pollForResponse(requestId);
            if (optionalMessage.isPresent()) {
                Message parsedMessage = optionalMessage.get();
                Blockchain blockchainReceived = new Gson().fromJson(parsedMessage.getJsonData(), Blockchain.class);
                return blockchainReceived;
            }
        } catch (Exception e) {
            LOGGER.error("Exception thrown: ", e);
        }
        return null;
    }


    private void submitEnergyOfferToSmartContract() {
        int expectedEnergyProduction = generateExpectedEnergyProduction();
        int energyUnitPrice = generateEnergyUnitPrice();
        // TODO: pack and send
    }


    private void processIncomingWriteBlockMessage(String peerAddress, int peerPort, String writeBlockMessage) {
        // TODO: get block from message
        //  Message message = new Gson().fromJson(writeBlockMessage, Message.class);
        //  Block block = message.getBlock();
        Block block = new Gson().fromJson(writeBlockMessage, Block.class);
        if (block.getIndex() != this.blockchain.getLast().getIndex() + 1) {
            updateBlockChainFromPeer(peerPort);
        } else {
            if (block.isValid()) {
                addNewBlock(block);
                this.recentActivity.add(new Activity(block.getHash(), peerAddress, Activity.DIRECTION.FROM));
            }
        }
    }


    protected void acceptRequests() {
        try {
            while (true) {
                // Listen for a TCP connection request.
                Socket connection = this.server.accept();

                // Construct an object to process the HTTP request message.
                HttpRequestHandler request = new HttpRequestHandler(connection);
                String content = new String(request.getSocket().getInputStream().readAllBytes());
                JsonObject json = new JsonParser().parse(content).getAsJsonObject();
                receivedMessages.add(json.toString());

                Gson gson = new Gson();
                Message message = gson.fromJson(json, Message.class);
                switch (message.getType()) {
                    case MessageType.STATUS: {
                        String myStatus = gson.toJson(this.status);
                        String myMessage = new Message(
                                message.getId()
                                , MessageType.RESPONSE
                                , myStatus
                                , this.getPort()).toJson();
                        sendMessage("localhost", message.getOriginPort(), myMessage);
                        break;
                    }
                    case MessageType.BLOCKCHAIN: {
                        String myBlockchain = gson.toJson(this.blockchain);
                        String myMessage = new Message(
                                message.getId()
                                , MessageType.RESPONSE
                                , myBlockchain
                                , this.getPort()).toJson();
                        sendMessage("localhost", message.getOriginPort(), myMessage);
                        break;
                    }
                    case MessageType.RESPONSE: {
                        LOGGER.info("Received response: " + message.toJson());
                        break;
                    }
                    default: {
                        LOGGER.warn("Not implemented. Message: " + message.toJson());
                        break;
                    }
                }
                // Create a new thread to process the request.
//                Thread thread = new Thread(request);
//                thread.start();
//                System.out.println("Thread started for " + this.port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addNewBlock(Block block) {
        this.blockchain.add(block);
        this.status = new Status(
                this.status.getTotalHashDifficulty()
                        + block.getHashDifficulty()
        );
        if (this.blockchain.getDifficulty() != this.status.getTotalHashDifficulty()) {
            throw new RuntimeException("Difficulties do not match");
        }
    }


    public void replaceBlockchain(Blockchain newBlockchain) {
        this.blockchain = newBlockchain;
        this.status = new Status(this.blockchain.getDifficulty());
    }


    public Status getStatus() {
        return status;
    }


    public List<Integer> getPortsOfOtherPeers() {
        return portsOfOtherPeers;
    }
}

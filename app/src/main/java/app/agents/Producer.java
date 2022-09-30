package app.agents;

import app.communication.PeerNode;
import app.messages.Message;
import app.messages.MessageType;
import app.properties.*;
import app.utilities.UUIDGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Hex;
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
        this.status = new Status(0);
        this.addGenesisBlock();
        this.updateBlockChain();
    }

    private void addGenesisBlock() {
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
        Status statusOfOtherPeer = askOtherPeerForStatus(peerPort);//read_request(status object)
        if (statusOfOtherPeer == null) {
            return;
        }
        //[self.status.total_difficulty < client.status.total_difficulty]
        if (statusOfOtherPeer.getTotalHashDifficulty() > this.status.getTotalHashDifficulty()) {
            //read_request(block object linked list)
            Blockchain otherPeerBlockchain = askOtherPeerForBlockchain(peerPort);
            //[received block object linked list is valid]
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
//            LOGGER.info("Waiting to get status");
            if (System.currentTimeMillis() - start > TIMEOUT) {
//                LOGGER.warn("Did not receive response after ms: " + TIMEOUT);
                break;
            }
            Thread.sleep(200);
        }
        return optReceivedMessage;
    }


    private Optional<Message> getMatchingResponse(String requestId) {
        Gson gson = new Gson();
        List<String> receivedMessages = getReceivedResponses();
        for (String receivedMessage : receivedMessages) {
            Message parsedMessage = gson.fromJson(receivedMessage, Message.class);
            if (parsedMessage.getId().equals(requestId)) {
                getReceivedResponses().remove(receivedMessage);
                return Optional.of(parsedMessage);
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


    private void processIncomingWriteBlockMessage(String peerAddress, Message writeBlockMessage) {
        Block block = new Gson().fromJson(writeBlockMessage.getJsonData(), Block.class);//validate_received_block_object() -> Sequence diagram
        if (block.getIndex() != this.blockchain.getLast().getIndex() + 1) {
            updateBlockChainFromPeer(writeBlockMessage.getOriginPort());
        }
        //[received_block is valid]
        else if (block.isValid()) {
            addNewBlock(block);
            this.recentActivity.add(new Activity(block.getHash(), peerAddress, Activity.DIRECTION.FROM));
        }
    }


    protected void acceptRequests() {
        try {
            while (true) {
                // Listen for a TCP connection request.
                Socket connection = this.server.accept();
                // Create a new thread to process the request.
                Thread thread = new Thread(() -> processRequest(connection));
                thread.start();
            }
        } catch (Exception e) {
            LOGGER.error("Exception thrown: ", e);
        }
    }


    private void processRequest(Socket connection) {
        try {
            LOGGER.info("PortA: " + this.getPort());

            // Construct an object to process the HTTP request message.
            String content = new String(connection.getInputStream().readAllBytes());
            JsonObject json = new JsonParser().parse(content).getAsJsonObject();
            Gson gson = new Gson();
            Message message = gson.fromJson(json, Message.class);
            switch (message.getType()) {
                // The other peer wants to know our status
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
                // The other peer wants to know our blockchain
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
                // The other peer gives us a response we had requested. It will not be processed here.
                case MessageType.RESPONSE: {
                    LOGGER.info("Received response: " + message.toJson());
                    receivedResponses.add(json.toString());
                    break;
                }
                // The other peer gives us a new block
                case MessageType.RECEIVED_BLOCK: {
                    processIncomingWriteBlockMessage("localhost", message);
                    break;
                }
                default: {
                    LOGGER.warn("Not implemented. Message: " + message.toJson());
                    break;
                }
            }
        } catch (Exception e) {
            LOGGER.error("Exception: ", e);
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

        // Not done, but not necessary in our case due to everybody knowing anybody
        //[Activity(client.address, received_block.hash) not in circular queue]
        //Assuming Essent producerapplication thread interactionnot in queue
        //write_request(received_block)
    }


    public void generateAndBroadcastBlock() {
        // When this producer has an offer, he decides to create a block and broadcast it to its other peers.


        // Build block
        Block myNewBlock = new BidTransactionBlock(
                this.blockchain.getLast().getHash(), 5, this.blockchain.size(), "0",
                Hex.encodeHexString(this.wallet.getPublicKey().getEncoded()), "0", 1, generateExpectedEnergyProduction(),
                generateEnergyUnitPrice(), new byte[0]);

        addNewBlock(myNewBlock);

        // Broadcast it
        broadcastBlock(myNewBlock);
    }


    public void broadcastBlock(Block block) {
        Message message = new Message(UUIDGenerator.generateUUID(), MessageType.RECEIVED_BLOCK, block.toJson(), this.getPort());
        for (int port : this.portsOfOtherPeers) {
            try {
                sendMessage("localhost", port, message.toJson());
            } catch (Exception e) {
                LOGGER.error("Exception thrown: ", e);
            }
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

    public Blockchain getBlockchain() {
        return blockchain;
    }
}

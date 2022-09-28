package app.agents;

import app.properties.*;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Functionality:
 * o   Generate expected energy production
 * o   Generate energy unit price required to make profit
 * o   Update stored blockchain (i.e., replace chain)
 * o   Submit energy offer to smart contract
 */
public class Producer {
    private List<String> portsOfOtherPeers = new ArrayList<>();//TODO: Get somehow
    private Random random = new Random();

    private RecentActivity recentActivity = new RecentActivity();
    private Blockchain blockchain = new Blockchain();
    private Wallet wallet = new Wallet(); // Let's have them, right for now
    private Battery battery = new Battery(random.nextInt(), random.nextInt());
    private Status status;
    private Offer offer;
    private Activity activity;


    public Producer() {
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
        for (String peerPort : portsOfOtherPeers) {
            updateBlockChainFromPeer(peerPort);
        }
    }

    private void updateBlockChainFromPeer(String peerPort) {
        Status statusOfOtherPeer = askOtherPeerForStatus(peerPort);
        if (statusOfOtherPeer.getTotalHashDifficulty() > this.status.getTotalHashDifficulty()) {
            Blockchain otherPeerBlockchain = askOtherPeerForBlockchain(peerPort);
            if (otherPeerBlockchain.isValid()) {
                this.blockchain = otherPeerBlockchain;
                this.status = new Status(this.blockchain.getLast().getHashDifficulty());
            }
        }
    }


    private Status askOtherPeerForStatus(String portOfPeerToAsk) {
        //TODO: iterate through the peers and ask them for their statuses
        return null;
    }

    private Blockchain askOtherPeerForBlockchain(String portOfPeerToAsk) {
        //TODO: ask peer for blockchain
        //TODO: Deserialize it
        return null;
    }


    private void submitEnergyOfferToSmartContract() {
        int expectedEnergyProduction = generateExpectedEnergyProduction();
        int energyUnitPrice = generateEnergyUnitPrice();
        // TODO: pack and send
    }


    private void processIncomingWriteBlockMessage(String peerAddress, String peerPort, String writeBlockMessage) {
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

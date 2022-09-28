package app.agents;

import app.properties.*;
import app.utilities.UUIDGenerator;

import java.util.Random;


/**
 * Functionality:
 * o   Generate expected energy production
 * o   Generate energy unit price required to make profit
 * o   Update stored blockchain (i.e., replace chain)
 * o   Submit energy offer to smart contract
 */
public class Producer {
    private Random random = new Random();
    private Status status;
    private Offer offer;
    private Blockchain blockchain = new Blockchain();
    private RecentActivity recentActivity = new RecentActivity();
    private Activity activity;
    private Wallet wallet = new Wallet(); // Let's have them, right for now
    private Battery battery = new Battery(random.nextInt(), random.nextInt());


    public Producer() {
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

    private synchronized void updateStoredBlockChain() {
        Block receivedBlock = null;

        if (receivedBlock.getHashDifficulty() > this.blockchain.peekLast().getHashDifficulty()) {
            this.blockchain.removeLast();
            this.blockchain.add(receivedBlock);
        }
    }

    private void submitEnergyOfferToSmartContract() {
        int expectedEnergyProduction = generateExpectedEnergyProduction();
        int energyUnitPrice = generateEnergyUnitPrice();
        // TODO: pack and send
    }
}

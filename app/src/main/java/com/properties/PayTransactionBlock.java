package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
public class PayTransactionBlock {
    private String hash;
    private String previousBlockIde;
    private String receiverAddress;
    private String publicKeySender;
    private String publicKeyReceiver;
    private int hashDifficult;
    private int coinAmount;
    private int energyAmountToEmit;

    private long deadline;
    private long timestamp;
    private byte[] signature;
    private int index;

    public PayTransactionBlock(String hash, String previousBlockIde, String receiverAddress, String publicKeySender, String publicKeyReceiver, int hashDifficult, int coinAmount, int energyAmountToEmit, long deadline, long timestamp, byte[] signature, int index) {
        this.hash = hash;
        this.previousBlockIde = previousBlockIde;
        this.receiverAddress = receiverAddress;
        this.publicKeySender = publicKeySender;
        this.publicKeyReceiver = publicKeyReceiver;
        this.hashDifficult = hashDifficult;
        this.coinAmount = coinAmount;
        this.energyAmountToEmit = energyAmountToEmit;
        this.deadline = deadline;
        this.timestamp = timestamp;
        this.signature = signature;
        this.index = index;
    }
}

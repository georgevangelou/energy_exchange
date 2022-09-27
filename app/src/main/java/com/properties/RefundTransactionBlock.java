package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
public class RefundTransactionBlock {
    //Refund transaction block: LWM2M object
    private String hash;
    private String previousBlockHash;
    private String receiverAddress;
    private String publicKeySender;
    private String publicKeyReceiver;
    private int hashDifficulty;
    private int coinAmount;
    private byte[] signature;
    private long timestamp;
    private int index;

    public RefundTransactionBlock(String hash, String previousBlockHash, String receiverAddress, String publicKeySender, String publicKeyReceiver, int hashDifficulty, int coinAmount, byte[] signature, long timestamp, int index) {
        this.hash = hash;
        this.previousBlockHash = previousBlockHash;
        this.receiverAddress = receiverAddress;
        this.publicKeySender = publicKeySender;
        this.publicKeyReceiver = publicKeyReceiver;
        this.hashDifficulty = hashDifficulty;
        this.coinAmount = coinAmount;
        this.signature = signature;
        this.timestamp = timestamp;
        this.index = index;
    }
}


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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreviousBlockIde() {
        return previousBlockIde;
    }

    public void setPreviousBlockIde(String previousBlockIde) {
        this.previousBlockIde = previousBlockIde;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getPublicKeySender() {
        return publicKeySender;
    }

    public void setPublicKeySender(String publicKeySender) {
        this.publicKeySender = publicKeySender;
    }

    public String getPublicKeyReceiver() {
        return publicKeyReceiver;
    }

    public void setPublicKeyReceiver(String publicKeyReceiver) {
        this.publicKeyReceiver = publicKeyReceiver;
    }

    public int getHashDifficult() {
        return hashDifficult;
    }

    public void setHashDifficult(int hashDifficult) {
        this.hashDifficult = hashDifficult;
    }

    public int getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(int coinAmount) {
        this.coinAmount = coinAmount;
    }

    public int getEnergyAmountToEmit() {
        return energyAmountToEmit;
    }

    public void setEnergyAmountToEmit(int energyAmountToEmit) {
        this.energyAmountToEmit = energyAmountToEmit;
    }

    public long getDeadline() {
        return deadline;
    }

    public void setDeadline(long deadline) {
        this.deadline = deadline;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

package com.properties;


public class BidTransactionBlock extends Block{
    private String receiverAddress;
    private String publicKeySender;
    private String privateKeySender;
    private int coinAmount;
    private int requestedEnergyAmount;
    private int requestedPricePerEnergyUnit;
    private byte[] signature;

    public BidTransactionBlock(String blockHash, String previousBlockHash, int hashDifficulty, long timestamp, int index, String receiverAddress, String publicKeySender, String privateKeySender, int coinAmount, int requestedEnergyAmount, int requestedPricePerEnergyUnit, byte[] signature) {
        super(blockHash, previousBlockHash, hashDifficulty, timestamp, index);
        this.receiverAddress = receiverAddress;
        this.publicKeySender = publicKeySender;
        this.privateKeySender = privateKeySender;
        this.coinAmount = coinAmount;
        this.requestedEnergyAmount = requestedEnergyAmount;
        this.requestedPricePerEnergyUnit = requestedPricePerEnergyUnit;
        this.signature = signature;
    }
}

package com.properties;


public class Offer {
    private int totalTransferableEnergyAmount = 0;
    private int pricePerEnergyUnit = 0;
    private int producerAddress = 0;

    public Offer(int totalTransferableEnergyAmount, int pricePerEnergyUnit, int producerAddress) {
        this.totalTransferableEnergyAmount = totalTransferableEnergyAmount;
        this.pricePerEnergyUnit = pricePerEnergyUnit;
        this.producerAddress = producerAddress;
    }

}

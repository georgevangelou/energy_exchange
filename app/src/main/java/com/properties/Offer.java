package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
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

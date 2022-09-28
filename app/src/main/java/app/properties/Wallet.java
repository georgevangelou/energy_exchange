package app.properties;

import app.utilities.UUIDGenerator;

public class Wallet {
    private int coinBalance;
    private String publicKey;
    private String privateKey;


    public Wallet() {
        this.coinBalance = 0;
        this.publicKey = UUIDGenerator.generateUUID();
        this.privateKey = UUIDGenerator.generateUUID();
    }

}

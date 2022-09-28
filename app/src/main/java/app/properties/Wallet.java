package app.properties;

public class Wallet {
    private int coinBalance;
    private String publicKey;

    public Wallet(int coinBalance, String publicKey, String privateKey) {
        this.coinBalance = coinBalance;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    private String privateKey;
}

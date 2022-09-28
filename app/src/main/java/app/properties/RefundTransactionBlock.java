package app.properties;


public class RefundTransactionBlock extends Block {
    private String receiverAddress;
    private String publicKeySender;
    private String publicKeyReceiver;
    private int coinAmount;
    private byte[] signature;

    public RefundTransactionBlock(String previousBlockHash, String receiverAddress, String publicKeySender, String publicKeyReceiver, int hashDifficulty, int coinAmount, byte[] signature, long timestamp, int index) {
        super(previousBlockHash, hashDifficulty, timestamp, index);
        this.receiverAddress = receiverAddress;
        this.publicKeySender = publicKeySender;
        this.publicKeyReceiver = publicKeyReceiver;
        this.coinAmount = coinAmount;
        this.signature = signature;
    }
}


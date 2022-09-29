package app.properties;


public class PayTransactionBlock extends Block {
    private String receiverAddress;
    private String publicKeySender;
    private String publicKeyReceiver;
    private int coinAmount;
    private int energyAmountToEmit;

    private long deadline;
    private byte[] signature;

    public PayTransactionBlock(String previousBlockHash, String receiverAddress, String publicKeySender, String publicKeyReceiver, int hashDifficulty, int coinAmount, int energyAmountToEmit, long deadline, byte[] signature, int index) {
        super(previousBlockHash, hashDifficulty, index);
        this.receiverAddress = receiverAddress;
        this.publicKeySender = publicKeySender;
        this.publicKeyReceiver = publicKeyReceiver;
        this.coinAmount = coinAmount;
        this.energyAmountToEmit = energyAmountToEmit;
        this.deadline = deadline;
        this.signature = signature;
    }
}

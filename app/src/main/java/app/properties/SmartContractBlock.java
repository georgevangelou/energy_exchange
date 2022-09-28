package app.properties;


public class SmartContractBlock extends Block {
    private String smartContractAddress;
    private String functions;
    private String functionParameters;

    public SmartContractBlock(String previousBlockHash, int hashDifficulty, String smartContractAddress, String functions, String functionParameters, int index) {
        super(previousBlockHash, hashDifficulty, index);
        this.smartContractAddress = smartContractAddress;
        this.functions = functions;
        this.functionParameters = functionParameters;
    }
}

package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
public class SmartContractBlock extends Block {
    private String smartContractAddress;
    private String functions;
    private String functionParameters;

    public SmartContractBlock(String blockHash, String previousBlockHash, int hashDifficulty, String smartContractAddress, String functions, String functionParameters, long timestamp, int index) {
        super(blockHash, previousBlockHash, hashDifficulty, timestamp, index);
        this.smartContractAddress = smartContractAddress;
        this.functions = functions;
        this.functionParameters = functionParameters;
    }
}

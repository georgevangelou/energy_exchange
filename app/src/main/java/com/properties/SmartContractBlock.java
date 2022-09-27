package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
public class SmartContractBlock {
    private String hash;
    private String previousBlockHash;
    private int hashDifficulty;
    private String smartContractAddress;
    private String functions;
    private String functionParameters;
    private long timestamp;
    private int index;

    public SmartContractBlock(String hash, String previousBlockHash, int hashDifficulty, String smartContractAddress, String functions, String functionParameters, long timestamp, int index) {
        this.hash = hash;
        this.previousBlockHash = previousBlockHash;
        this.hashDifficulty = hashDifficulty;
        this.smartContractAddress = smartContractAddress;
        this.functions = functions;
        this.functionParameters = functionParameters;
        this.timestamp = timestamp;
        this.index = index;
    }
}

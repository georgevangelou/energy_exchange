package com.properties;

/**
 * @author Georgios Evangelou
 * Created on: 2022-09-27
 */
public abstract class Block {
    private String blockHash;
    private String previousBlockHash;
    private int hashDifficulty;
    private long timestamp;
    private int index;

    public Block(String blockHash, String previousBlockHash, int hashDifficulty, long timestamp, int index) {
        this.blockHash = blockHash;
        this.previousBlockHash = previousBlockHash;
        this.hashDifficulty = hashDifficulty;
        this.timestamp = timestamp;
        this.index = index;
    }
}

package app.properties;


import app.utilities.StringUtil;
import com.google.gson.Gson;

import java.util.Date;

public abstract class Block {
    private String hash;
    private String previousBlockHash;
    private int hashDifficulty;
    private long timestamp;
    private int index;


    public Block(String previousBlockHash, int hashDifficulty, int index) {
        this.previousBlockHash = previousBlockHash;
        this.hashDifficulty = hashDifficulty; // TODO: we should calculate it somehow
        this.timestamp = new Date().getTime();
        this.index = index;
        this.hash = hash();
    }

    private String hash() {
        return StringUtil.applySha256(
                this.previousBlockHash +
                        Long.toString(this.timestamp) +
                        Integer.toString(this.index)
        );
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }


    public String getHash() {
        return this.hash;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public int getHashDifficulty() {
        return hashDifficulty;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getIndex() {
        return index;
    }

    public boolean isValid(){
        return true;// TODO
    }
}

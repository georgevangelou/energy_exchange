package app.properties;


import com.google.gson.Gson;

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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}

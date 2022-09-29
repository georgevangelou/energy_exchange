package app.properties;

/**
 * This MUST always be updated as per the difficulty of the blockchain of its owner.
 */
public class Status {
    private int totalHashDifficulty;

    public Status(int totalHashDifficulty) {
        this.totalHashDifficulty = totalHashDifficulty;
    }

    public int getTotalHashDifficulty() {
        return totalHashDifficulty;
    }
}

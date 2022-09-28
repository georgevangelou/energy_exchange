package app.properties;

public class Status {
    private int totalHashDifficulty;

    public Status(int totalHashDifficulty) {
        this.totalHashDifficulty = totalHashDifficulty;
    }

    public int getTotalHashDifficulty() {
        return totalHashDifficulty;
    }
}

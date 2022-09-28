package app.properties;

public class Activity {
    private String activityHash;
    private String clientAddress;
    private DIRECTION direction;

    public Activity(String activityHash, String clientAddress, DIRECTION direction) {
        this.activityHash = activityHash;
        this.clientAddress = clientAddress;
        this.direction = direction;
    }

    public enum DIRECTION {
        TO, FROM
    }
}

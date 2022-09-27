package com.properties;

public class Activity {
    private String activityHash;
    private String clientAddress;
    private boolean toFrom_;

    public Activity(String activityHash, String clientAddress, boolean toFrom_) {
        this.activityHash = activityHash;
        this.clientAddress = clientAddress;
        this.toFrom_ = toFrom_;
    }
}

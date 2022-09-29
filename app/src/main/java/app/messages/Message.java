package app.messages;

import com.google.gson.Gson;

import java.util.Date;

public class Message {
    private String id;
    private String type;
    private int originPort;
    private long timestamp;
    private String jsonData;

    public Message(String id, String type, String jsonData, int originPort) {
        this.id = id;
        this.type = type;
        this.jsonData = jsonData;
        this.originPort = originPort;
        this.timestamp = new Date().getTime();
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String getId() {
        return id;
    }

    public String getJsonData() {
        return jsonData;
    }

    public String getType() {
        return type;
    }

    public int getOriginPort() {
        return originPort;
    }

    public long getTimestamp() {
        return timestamp;
    }
}

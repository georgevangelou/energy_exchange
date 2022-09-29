package app.investigations;

import com.google.gson.Gson;

import java.util.Date;

public class Message {
    private String id;
    private int port;
    private long timestamp;

    private String jsonData;

    public Message(String id, String jsonData, int port) {
        this.id = id;
        this.jsonData = jsonData;
        this.port = port;
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
}
